package com.kaaneneskpc.travellerr.payment

import android.content.Context
import com.stripe.android.PaymentConfiguration
import org.koin.core.context.GlobalContext

actual class StripePaymentHandler actual constructor(){
    actual fun initialize(publishableKey: String) {
        val context = GlobalContext.get().get<Context>()
        PaymentConfiguration.init(context,publishableKey)
    }

    actual suspend fun processPayment(clientSecret: String): PaymentResult {
        val holder = PaymentSheetHolder.instance
        return holder?.present(clientSecret) ?: PaymentResult.Failure("PaymentSheetHolder not initialized")
    }
}