package com.kaaneneskpc.presentation.feature.bookings

import com.kaaneneskpc.domain.model.Booking
import com.kaaneneskpc.domain.model.TravelListing

data class BookingListUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val bookings: List<Booking> = emptyList(),
    val listingsMap: Map<String, TravelListing> = emptyMap()
)