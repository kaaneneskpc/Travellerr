package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.PaymentIntent
import com.kaaneneskpc.domain.repository.PaymentRepository

class CreatePaymentIntentUseCase(private val repository: PaymentRepository) {
    suspend fun execute(
        bookingID: String,
        amount: Double,
        currency: String
    ): PaymentIntent? {
        val data = repository.createPaymentIntent(bookingID,amount,currency)
        return if (data.isSuccess) {
            data.getOrNull()
        } else {
            null
        }
    }
}