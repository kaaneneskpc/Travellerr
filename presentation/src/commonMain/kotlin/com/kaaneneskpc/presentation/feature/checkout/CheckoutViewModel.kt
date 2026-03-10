package com.kaaneneskpc.presentation.feature.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.domain.usecase.CheckAvailabilityUseCase
import com.kaaneneskpc.domain.usecase.CreateBookingUseCase
import com.kaaneneskpc.domain.usecase.CreatePaymentIntentUseCase
import com.kaaneneskpc.domain.usecase.GetListingByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel(
    val itemID: String, val getListingByIdUseCase: GetListingByIdUseCase,
    private val checkAvailabilityUseCase: CheckAvailabilityUseCase,
    private val createBookingUseCase: CreateBookingUseCase,
    private val createPaymentIntentUseCase: CreatePaymentIntentUseCase
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getListingDetails()
    }

    fun getListingDetails() {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(isLoading = true)
            val item = getListingByIdUseCase.execute(itemID)
            if (item == null) {
                _uiState.value = uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load item details"
                )
            } else {
                _uiState.value = uiState.value.copy(
                    isLoading = false,
                    listing = item
                )
            }
        }
    }

    fun selectTripDate(tripDateId: String) {
        val currentBooking = _uiState.value.booking
        _uiState.value = uiState.value.copy(
            selectedTripDateId = tripDateId,
            booking = if (currentBooking?.tripDateId == tripDateId) currentBooking else null,
            paymentIntent = null,
            hasDateConflict = false,
            bookingAvailability = null,
            availabilityErrorMessage = null
        )
        checkAvailability()
    }

    fun addGuest() {
        if (_uiState.value.numberOfGuests < 10) {
            _uiState.value = uiState.value.copy(
                numberOfGuests = uiState.value.numberOfGuests + 1,
                booking = null,
                paymentIntent = null
            )
        }
        checkAvailability()
    }

    fun removeGuest() {
        if (_uiState.value.numberOfGuests > 1) {
            _uiState.value = uiState.value.copy(
                numberOfGuests = uiState.value.numberOfGuests - 1,
                booking = null,
                paymentIntent = null
            )
        }
        checkAvailability()
    }


    fun checkAvailability() {
        val listing = uiState.value.listing
        val tripDateId = uiState.value.selectedTripDateId
        val numberOfGuests = uiState.value.numberOfGuests

        if (listing == null || tripDateId == null) {
            _uiState.value = uiState.value.copy(
                availabilityErrorMessage = "Please select a trip date",
                bookingAvailability = null,
                hasDateConflict = false
            )
            return
        }

        val selectedTripDate = listing.tripDates?.find { it.id == tripDateId }
        if (selectedTripDate == null) {
            _uiState.value = uiState.value.copy(
                availabilityErrorMessage = "Selected trip date not found",
                bookingAvailability = null,
                hasDateConflict = false
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = uiState.value.copy(
                isCheckingAvailability = true,
                availabilityErrorMessage = null,
                bookingAvailability = null,
                hasDateConflict = false
            )
            try {
                val result = checkAvailabilityUseCase.execute(
                    listingId = listing.id,
                    tripDateId = tripDateId,
                    checkInDate = selectedTripDate.startDate,
                    checkOutDate = selectedTripDate.endDate,
                    noOfPeople = numberOfGuests
                )
                if (result.isFailure) {
                    val errorMsg = result.exceptionOrNull()?.message ?: "Unknown error"
                    val isConflict = isDateConflictError(errorMsg)
                    _uiState.value = uiState.value.copy(
                        isCheckingAvailability = false,
                        availabilityErrorMessage = if (isConflict) {
                            "You have a pending booking for these dates."
                        } else {
                            "Failed: $errorMsg"
                        },
                        bookingAvailability = null,
                        hasDateConflict = isConflict
                    )
                } else {
                    val availability = result.getOrNull()
                    val isConflict = availability?.available == false &&
                            isDateConflictError(availability.reason.orEmpty())
                    _uiState.value = uiState.value.copy(
                        isCheckingAvailability = false,
                        availabilityErrorMessage = when {
                            isConflict -> "You have a pending booking for these dates."
                            availability?.available == false -> availability.reason
                            else -> null
                        },
                        bookingAvailability = if (availability?.available == true) availability else null,
                        hasDateConflict = isConflict
                    )
                }
            } catch (ex: Exception) {
                val errorMsg = ex.message ?: "Unknown error"
                val isConflict = isDateConflictError(errorMsg)
                _uiState.value = uiState.value.copy(
                    isCheckingAvailability = false,
                    availabilityErrorMessage = if (isConflict) {
                        "You have a pending booking for these dates."
                    } else {
                        "Exception: $errorMsg"
                    },
                    bookingAvailability = null,
                    hasDateConflict = isConflict
                )
            }
        }
    }

    private fun isDateConflictError(message: String): Boolean {
        val lowerMessage = message.lowercase()
        return lowerMessage.contains("conflict") ||
                lowerMessage.contains("already booked") ||
                lowerMessage.contains("already exists") ||
                lowerMessage.contains("409")
    }

    fun createBooking() {
        val existingBooking = uiState.value.booking
        val tripDateId = uiState.value.selectedTripDateId

        if (existingBooking != null && existingBooking.tripDateId == tripDateId) {
            createPaymentIntentForBooking(existingBooking)
            return
        }

        val listing = uiState.value.listing
        val numberOfGuests = uiState.value.numberOfGuests

        if (listing == null || tripDateId == null) {
            _uiState.value = uiState.value.copy(
                availabilityErrorMessage = "Please select a trip date",
                bookingAvailability = null
            )
            return
        }

        val selectedTripDate = listing.tripDates?.find { it.id == tripDateId }
        if (selectedTripDate == null) {
            _uiState.value = uiState.value.copy(
                availabilityErrorMessage = "Selected trip date not found",
                paymentIntent = null
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = uiState.value.copy(
                creatingBooking = true,
                availabilityErrorMessage = null,
                paymentIntent = null
            )

            val booking = createBookingUseCase.execute(
                listingId = listing.id,
                tripDateId = tripDateId,
                checkInDate = selectedTripDate.startDate,
                checkOutDate = selectedTripDate.endDate,
                noOfPeople = numberOfGuests
            )

            if (booking == null) {
                _uiState.value = uiState.value.copy(
                    creatingBooking = false,
                    availabilityErrorMessage = "Failed to create booking",
                    paymentIntent = null
                )
            } else {
                _uiState.value = uiState.value.copy(booking = booking)
                createPaymentIntentForBooking(booking)
            }
        }
    }

    private fun createPaymentIntentForBooking(booking: com.kaaneneskpc.domain.model.Booking) {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(
                creatingBooking = true,
                availabilityErrorMessage = null,
                paymentIntent = null
            )
            val paymentIntent = createPaymentIntentUseCase.execute(
                bookingID = booking.id,
                amount = booking.totalPrice,
                currency = booking.currency
            )
            if (paymentIntent == null) {
                _uiState.value = uiState.value.copy(
                    creatingBooking = false,
                    availabilityErrorMessage = "Failed to create payment intent",
                    paymentIntent = null
                )
            } else {
                _uiState.value = uiState.value.copy(
                    creatingBooking = false,
                    availabilityErrorMessage = null,
                    paymentIntent = paymentIntent
                )
            }
        }
    }

    fun resetPaymentState() {
        _uiState.value = uiState.value.copy(paymentIntent = null)
    }
}