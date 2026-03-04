package com.kaaneneskpc.presentation.feature.signIn

import com.kaaneneskpc.domain.model.UserModel

data class SignInUiState(
    val user: UserModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)