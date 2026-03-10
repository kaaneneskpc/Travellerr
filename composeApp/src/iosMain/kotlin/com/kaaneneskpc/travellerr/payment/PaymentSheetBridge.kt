package com.kaaneneskpc.travellerr.payment

object PaymentSheetBridge {
    var initializeFunction : ((String) -> Unit)? = null
    var processPaymentFunction : ((String, (String) -> Unit) -> Unit)? = null
}