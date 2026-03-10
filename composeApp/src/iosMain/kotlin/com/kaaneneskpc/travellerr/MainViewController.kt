package com.kaaneneskpc.travellerr

import androidx.compose.ui.window.ComposeUIViewController
import com.kaaneneskpc.travellerr.di.appModule
import com.kaaneneskpc.travellerr.payment.PaymentSheetBridge
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(configure = {
    startKoin { modules(appModule) }
}) { App() }

fun registerBridge(
    initialize: (String) -> Unit,
    processPayment: (String, (String) -> Unit) -> Unit
) {
    PaymentSheetBridge.initializeFunction = initialize
    PaymentSheetBridge.processPaymentFunction = processPayment
}
