package com.kaaneneskpc.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BookingDto(
    val checkInDate: String? = null,
    val checkOutDate: String? = null,
    val createdAt: String,
    val currency: String,
    val customerId: String,
    val id: String,
    val listingId: String,
    val numberOfGuests: Int,
    val paymentId: String? = null,
    val paymentStatus: String,
    val specialRequests: String? = null,
    val status: String,
    val totalPrice: Double,
    val tripDateId: String,
    val updatedAt: String,
    val listing: ListingSummaryDto? = null,
    val tripDate: TripDateSummaryDto? = null
)