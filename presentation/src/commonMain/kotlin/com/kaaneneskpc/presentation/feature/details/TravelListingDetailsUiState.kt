package com.kaaneneskpc.presentation.feature.details

import com.kaaneneskpc.domain.model.TravelListing


data class TravelListingDetailsUiState(
    val listing: TravelListing? = null,
    val isLoading:Boolean = false,
    val errorMessage:String? = null
) {

}