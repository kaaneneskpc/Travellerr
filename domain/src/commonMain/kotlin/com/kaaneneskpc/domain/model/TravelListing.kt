package com.kaaneneskpc.domain.model


data class TravelListing(
    val id: String,
    val vendorId: String,
    val title: String,
    val description: String,
    val category: String,
    val location: String,
    val city: String?,
    val country: String?,
    val price: Double,
    val currency: String,
    val capacity: Int?,
    val availableFrom: String?,
    val availableTo: String?,
    val images: List<String>?,
    val amenities: List<String>?,
    val rating: Double,
    val reviewCount: Int,
    val isActive: Boolean,
    val tripDates: List<TripDate>?
) {
}