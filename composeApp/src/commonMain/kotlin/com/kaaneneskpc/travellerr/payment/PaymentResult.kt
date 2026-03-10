package com.kaaneneskpc.travellerr.payment


sealed class PaymentResult {
    data object Success : PaymentResult()
    data class Failure(val errorMessage: String) : PaymentResult()
    data object Cancelled : PaymentResult()
}