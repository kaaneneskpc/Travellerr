package com.kaaneneskpc.data.model.response


import com.kaaneneskpc.data.model.TravelListingDto
import kotlinx.serialization.Serializable

@Serializable
data class ListingResponse(
    val listings: List<TravelListingDto>,
    val page: Int,
    val pageSize: Int,
    val total: Int
)