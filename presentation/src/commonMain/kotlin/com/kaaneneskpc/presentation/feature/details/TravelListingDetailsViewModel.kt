package com.kaaneneskpc.presentation.feature.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.domain.usecase.GetAllListingUseCase
import com.kaaneneskpc.domain.usecase.GetListingByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TravelListingDetailsViewModel(
    val getListingDetailsUseCase: GetListingByIdUseCase,
    val itemID: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(TravelListingDetailsUiState())
    val state = _uiState.asStateFlow()

    init {
        getListingDetails()
    }

    private fun getListingDetails() {
        viewModelScope.launch {
            _uiState.value = state.value.copy(isLoading = true)
            val item = getListingDetailsUseCase.execute(itemID)
            if (item == null) {
                _uiState.value =
                    state.value.copy(isLoading = false, errorMessage = "Error loading details")
            } else {
                _uiState.value = state.value.copy(isLoading = false, listing = item)
            }
            println(
                "Details ViewModel: Loaded item details for ID $itemID: ${_uiState.value.listing}"
            )
        }
    }

}