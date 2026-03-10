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
        _uiState.value = uiState.value.copy(selectedTripDateId = tripDateId)
        checkAvailability()
    }

    fun addGuest() {
        if (_uiState.value.numberOfGuests < 10) {
            _uiState.value = uiState.value.copy(numberOfGuests = uiState.value.numberOfGuests + 1)
        }
        checkAvailability()
    }

    fun removeGuest() {
        if (_uiState.value.numberOfGuests > 1) {
            _uiState.value = uiState.value.copy(numberOfGuests = uiState.value.numberOfGuests - 1)
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
                bookingAvailability = null
            )
            return
        }

        val selectedTripDate = listing.tripDates?.find { it.id == tripDateId }
        if (selectedTripDate == null) {
            _uiState.value = uiState.value.copy(
                availabilityErrorMessage = "Selected trip date not found",
                bookingAvailability = null
            )
            return
        }

        viewModelScope.launch {
            println("DEBUG_VM: ===== checkAvailability START =====")
            println("DEBUG_VM: listingId=${listing.id}")
            println("DEBUG_VM: tripDateId=$tripDateId")
            println("DEBUG_VM: checkInDate=${selectedTripDate.startDate}")
            println("DEBUG_VM: checkOutDate=${selectedTripDate.endDate}")
            println("DEBUG_VM: numberOfGuests=$numberOfGuests")
            _uiState.value = uiState.value.copy(
                isCheckingAvailability = true,
                availabilityErrorMessage = null,
                bookingAvailability = null
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
                    val exception = result.exceptionOrNull()
                    val errorMsg = exception?.message ?: "Unknown error"
                    println("DEBUG_VM: RESULT FAILURE: $errorMsg")
                    println("DEBUG_VM: Exception class: ${exception?.let { it::class.simpleName }}")
                    _uiState.value = uiState.value.copy(
                        isCheckingAvailability = false,
                        availabilityErrorMessage = "Failed: $errorMsg",
                        bookingAvailability = null
                    )
                } else {
                    println("DEBUG_VM: RESULT SUCCESS: ${result.getOrNull()}")
                    _uiState.value = uiState.value.copy(
                        isCheckingAvailability = false,
                        availabilityErrorMessage = null,
                        bookingAvailability = result.getOrNull()
                    )
                }
            } catch (ex: Exception) {
                println("DEBUG_VM: UNCAUGHT EXCEPTION: ${ex::class.simpleName}: ${ex.message}")
                ex.printStackTrace()
                _uiState.value = uiState.value.copy(
                    isCheckingAvailability = false,
                    availabilityErrorMessage = "Exception: ${ex.message}",
                    bookingAvailability = null
                )
            }
            println("DEBUG_VM: ===== checkAvailability END =====")
        }
    }

    fun createBooking() {
        val listing = uiState.value.listing
        val tripDateId = uiState.value.selectedTripDateId
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
                booking.let {
                    val paymentIntent = createPaymentIntentUseCase.execute(
                        bookingID = it.id,
                        amount = it.totalPrice,
                        currency =it.currency
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
        }
    }

}