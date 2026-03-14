package com.kaaneneskpc.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateBookingStatusRequest(
    val status: String
)
