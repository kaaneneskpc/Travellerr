package com.kaaneneskpc.presentation.feature.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.domain.usecase.GetAuthTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel (private val useCase: GetAuthTokenUseCase): ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val state = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadAuthToken()
        }
    }
    suspend fun loadAuthToken() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        val token = useCase.execute()
        _uiState.value = _uiState.value.copy(isLoading = false, authToken = token)
    }
}