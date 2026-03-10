package com.kaaneneskpc.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentIntentDto(
    val clientSecret: String,
    val paymentIntentId: String,
    val amount: Long,
    val currency: String,
    val status: String
)