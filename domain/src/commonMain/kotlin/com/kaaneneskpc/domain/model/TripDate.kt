package com.kaaneneskpc.domain.model

data class TripDate(
    val availableSpots: Int,
    val createdAt: String,
    val currentBookings: Int,
    val endDate: String,
    val id: String,
    val isActive: Boolean,
    val listingId: String,
    val maxCapacity: Int,
    val startDate: String,
    val updatedAt: String
)