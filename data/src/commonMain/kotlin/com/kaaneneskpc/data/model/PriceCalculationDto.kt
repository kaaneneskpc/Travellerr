package com.kaaneneskpc.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PriceCalculationDto(
    val subtotal: Double,
    val taxes: Double,
    val serviceFee: Double,
    val total: Double,
    val currency: String,
    val numberOfNights: Int,
    val numberOfGuests: Int
)