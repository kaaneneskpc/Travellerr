package com.kaaneneskpc.presentation.feature.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.domain.usecase.SignInUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignInViewModel(private val loginUseCase: SignInUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun signIn() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = loginUseCase.execute(_email.value, _password.value)
            result.onSuccess { user ->
                _uiState.value = _uiState.value.copy(user = user, isLoading = false)
            }.onFailure { error ->
                _uiState.value =
                    _uiState.value.copy(errorMessage = error.message, isLoading = false)
            }
        }
    }

}