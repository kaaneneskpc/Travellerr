package com.kaaneneskpc.data.model.response

import com.kaaneneskpc.data.model.UserDto
import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    val token: String,
    val user: UserDto
)