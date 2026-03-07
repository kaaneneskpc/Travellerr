package com.kaaneneskpc.presentation.feature.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.domain.usecase.GetAuthTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AppViewModel(private val useCase: GetAuthTokenUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val state = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            useCase.execute()
                .onStart { _uiState.value = _uiState.value.copy(isLoading = true) }
                .catch { _uiState.value = _uiState.value.copy(isLoading = false, authToken = null) }
                .collect { token ->
                    _uiState.value = _uiState.value.copy(isLoading = false, authToken = token)
                }
        }
    }
}