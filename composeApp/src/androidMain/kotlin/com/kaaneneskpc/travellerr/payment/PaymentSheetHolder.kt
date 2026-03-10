package com.kaaneneskpc.travellerr.payment

import androidx.activity.ComponentActivity
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PaymentSheetHolder(private val componentActivity: ComponentActivity) {

    var continuation: CancellableContinuation<PaymentResult>? = null

    val paymentsheet = PaymentSheet(componentActivity) {
        val result = when (it) {
            is PaymentSheetResult.Completed -> {
                PaymentResult.Success
            }

            is PaymentSheetResult.Canceled -> {
                PaymentResult.Cancelled
            }

            is PaymentSheetResult.Failed -> {
                PaymentResult.Failure(it.error.localizedMessage ?: "Unknown error")
            }
        }

        continuation?.resume(result)
        continuation = null

    }

    suspend fun present(clientSecret: String): PaymentResult {
        return suspendCancellableCoroutine {
            continuation = it
            paymentsheet.presentWithPaymentIntent(
                clientSecret,
                PaymentSheet.Configuration("Travenor")
            )
        }
    }

    companion object {
        var instance: PaymentSheetHolder? = null
    }
}