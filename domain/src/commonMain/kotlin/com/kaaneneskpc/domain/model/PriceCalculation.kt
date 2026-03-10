package com.kaaneneskpc.domain.model

data class PriceCalculation(
    val subtotal: Double,
    val taxes: Double,
    val serviceFee: Double,
    val total: Double,
    val currency: String,
    val numberOfNights: Int,
    val numberOfGuests: Int
)