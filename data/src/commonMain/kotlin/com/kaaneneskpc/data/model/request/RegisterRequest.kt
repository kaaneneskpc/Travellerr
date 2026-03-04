package com.kaaneneskpc.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val phone: String,
    val role: String
)