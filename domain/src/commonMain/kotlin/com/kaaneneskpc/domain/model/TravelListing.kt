package com.kaaneneskpc.domain.model


data class TravelListing(
    val id: String,
    val title: String,
    val location: String,
    val images: List<String>?,
    val rating: Double,
) {
}