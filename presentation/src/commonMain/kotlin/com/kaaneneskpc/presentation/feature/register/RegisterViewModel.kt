package com.kaaneneskpc.presentation.feature.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.domain.model.RegisterModel
import com.kaaneneskpc.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerUseCase: RegisterUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onNameChange(newName: String) {
        _name.value = newName
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
    }

    fun register() {
        viewModelScope.launch {
            if (_password.value != _confirmPassword.value) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Password and Confirm Password do not match"
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = registerUseCase.execute(
                RegisterModel(
                    firstName = name.value,
                    email = email.value,
                    password = password.value,
                )
            )
            result.onSuccess { user ->
                _uiState.value = _uiState.value.copy(user = user, isLoading = false)
            }.onFailure { error ->
                _uiState.value =
                    _uiState.value.copy(errorMessage = error.message, isLoading = false)
            }
        }
    }

}