package com.kaaneneskpc.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BookingAvailabilityDto(
    val available: Boolean,
    val reason: String? = null,
    val priceCalculation: PriceCalculationDto? = null
)