package com.kaaneneskpc.presentation.feature.register

import com.kaaneneskpc.domain.model.UserModel

data class RegisterUiState(
    val user: UserModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)