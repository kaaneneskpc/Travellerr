package com.kaaneneskpc.domain.model

data class PaymentIntent(
    val clientSecret: String,
    val paymentIntentId: String,
    val amount: Long,
    val currency: String,
    val status: String
)