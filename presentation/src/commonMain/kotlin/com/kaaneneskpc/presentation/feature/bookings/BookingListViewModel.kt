package com.kaaneneskpc.presentation.feature.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.domain.model.TravelListing
import com.kaaneneskpc.domain.usecase.GetAllBookingUseCase
import com.kaaneneskpc.domain.usecase.GetListingByIdUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookingListViewModel(
    private val bookingUseCase: GetAllBookingUseCase,
    private val getListingByIdUseCase: GetListingByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllBookings()
    }

    fun getAllBookings() {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(isLoading = true)
            val bookings = bookingUseCase.execute()
            if (bookings == null) {
                _uiState.value = uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load bookings"
                )
            } else {
                _uiState.value = uiState.value.copy(
                    isLoading = false,
                    bookings = bookings
                )
                fetchListingDetails(bookings.map { it.listingId }.distinct())
            }
        }
    }

    private fun fetchListingDetails(listingIds: List<String>) {
        viewModelScope.launch {
            val listingsMap = mutableMapOf<String, TravelListing>()
            val deferredListings = listingIds.map { listingId ->
                async {
                    val listing = getListingByIdUseCase.execute(listingId)
                    if (listing != null) {
                        listingId to listing
                    } else {
                        null
                    }
                }
            }
            deferredListings.awaitAll().filterNotNull().forEach { (id, listing) ->
                listingsMap[id] = listing
            }
            _uiState.value = uiState.value.copy(listingsMap = listingsMap)
        }
    }
}