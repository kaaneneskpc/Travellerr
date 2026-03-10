package com.kaaneneskpc.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class BookingInfoRequest(
    val listingId: String,
    val tripDateId: String? = null,
    val checkInDate: String? = null,
    val checkOutDate: String? = null,
    val numberOfGuests: Int = 1,
    val specialRequests: String? = null
)