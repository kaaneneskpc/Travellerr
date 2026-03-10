package com.kaaneneskpc.travellerr.payment

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class StripePaymentHandler actual constructor() {
    actual fun initialize(publishableKey: String) {
        val initFunction = PaymentSheetBridge.initializeFunction
        initFunction?.let {
            it(publishableKey)
        }
    }

    actual suspend fun processPayment(clientSecret: String): PaymentResult {
        val processFunction = PaymentSheetBridge.processPaymentFunction

        processFunction?.let {
            return suspendCancellableCoroutine { continuation ->
                it(clientSecret) { result ->
                    val paymentResult = when (result) {
                        "success" -> PaymentResult.Success
                        "failure" -> PaymentResult.Failure("Payment failed")
                        "canceled" -> PaymentResult.Cancelled
                        else -> PaymentResult.Failure("Unknown payment result")
                    }
                    continuation.resume(paymentResult)
                }
            }
        }
        return PaymentResult.Failure("Payment processing function not implemented")
    }
}