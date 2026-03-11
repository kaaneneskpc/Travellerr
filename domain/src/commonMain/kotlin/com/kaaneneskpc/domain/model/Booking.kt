package com.kaaneneskpc.domain.model

data class Booking(
    val checkInDate: String?,
    val checkOutDate: String?,
    val createdAt: String,
    val currency: String,
    val customerId: String,
    val id: String,
    val listingId: String,
    val numberOfGuests: Int,
    val paymentId: String?,
    val paymentStatus: String,
    val specialRequests: String?,
    val status: String,
    val totalPrice: Double,
    val tripDateId: String,
    val updatedAt: String,
    val listing: ListingSummary?,
    val tripDate: TripDateSummary?
)