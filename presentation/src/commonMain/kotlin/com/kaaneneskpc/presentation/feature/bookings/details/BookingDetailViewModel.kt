package com.kaaneneskpc.presentation.feature.bookings.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.domain.model.Booking
import com.kaaneneskpc.domain.model.TravelListing
import com.kaaneneskpc.domain.usecase.GetBookingByIdUseCase
import com.kaaneneskpc.domain.usecase.GetListingByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BookingDetailUiState(
    val isLoading: Boolean = false,
    val booking: Booking? = null,
    val listing: TravelListing? = null,
    val errorMessage: String? = null
)

class BookingDetailViewModel(
    private val getBookingByIdUseCase: GetBookingByIdUseCase,
    private val getListingByIdUseCase: GetListingByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun fetchBookingDetails(bookingId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = getBookingByIdUseCase.execute(bookingId)
            result.onSuccess { booking ->
                val listing = getListingByIdUseCase.execute(booking.listingId)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    booking = booking,
                    listing = listing
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load booking details: ${exception::class.simpleName} - ${exception.message}"
                )
            }
        }
    }
}
