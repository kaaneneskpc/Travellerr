package com.kaaneneskpc.travellerr.payment

expect class StripePaymentHandler() {
    fun initialize(publishableKey: String)
    suspend fun processPayment(clientSecret: String): PaymentResult
}