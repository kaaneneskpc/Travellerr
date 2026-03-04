package com.kaaneneskpc.presentation.listings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.domain.usecase.GetAllListingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class TravelListingViewModel(val getAllListingUseCase: GetAllListingUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(TravelListingUiState())
    val state = _uiState.asStateFlow()

    init {
        loadTravelListings()
    }

    fun loadTravelListings() {
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(isLoading = true, errorMessage = null, listings = emptyList())
            try {
                getAllListingUseCase.execute().collect { listings ->
                    _uiState.value = _uiState.value.copy(
                        listings = listings,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load listings: ${e.message}"
                )
            }
        }
    }

}