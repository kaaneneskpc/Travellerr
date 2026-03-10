package com.kaaneneskpc.domain.repository

import com.kaaneneskpc.domain.model.PaymentIntent


interface PaymentRepository {
    suspend fun createPaymentIntent(
        bookingId: String,
        amount: Double? = null,
        currency: String = "usd"
    ): Result<PaymentIntent>
}