package com.kaaneneskpc.presentation.feature.bookings.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.domain.model.Booking
import com.kaaneneskpc.domain.model.TravelListing
import com.kaaneneskpc.domain.usecase.GetBookingByIdUseCase
import com.kaaneneskpc.domain.usecase.GetListingByIdUseCase
import com.kaaneneskpc.domain.usecase.UpdateBookingStatusUseCase
import com.kaaneneskpc.domain.usecase.DeleteBookingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DeleteResult {
    data object Idle : DeleteResult()
    data object Success : DeleteResult()
    data class Failure(val message: String) : DeleteResult()
}

data class BookingDetailUiState(
    val isLoading: Boolean = false,
    val booking: Booking? = null,
    val listing: TravelListing? = null,
    val errorMessage: String? = null,
    val deleteResult: DeleteResult = DeleteResult.Idle
)

class BookingDetailViewModel(
    private val getBookingByIdUseCase: GetBookingByIdUseCase,
    private val getListingByIdUseCase: GetListingByIdUseCase,
    private val updateBookingStatusUseCase: UpdateBookingStatusUseCase,
    private val deleteBookingUseCase: DeleteBookingUseCase
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

    fun updateBookingStatus(bookingId: String, newStatus: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = updateBookingStatusUseCase.execute(bookingId, newStatus)
            result.onSuccess {
                fetchBookingDetails(bookingId)
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to update booking status: ${exception::class.simpleName} - ${exception.message}"
                )
            }
        }
    }

    fun deleteBooking(bookingId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = deleteBookingUseCase.execute(bookingId)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    deleteResult = DeleteResult.Success
                )
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    deleteResult = DeleteResult.Failure("Failed to delete booking: ${exception::class.simpleName} - ${exception.message}")
                )
            }
        }
    }

    fun resetDeleteResult() {
        _uiState.value = _uiState.value.copy(deleteResult = DeleteResult.Idle)
    }
}
