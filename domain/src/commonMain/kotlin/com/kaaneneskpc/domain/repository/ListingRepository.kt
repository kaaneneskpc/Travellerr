package com.kaaneneskpc.domain.repository

import com.kaaneneskpc.domain.model.TravelListing
import kotlinx.coroutines.flow.Flow

interface ListingRepository {
    suspend fun getAllListings(): Result<List<TravelListing>>
}