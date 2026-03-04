package com.kaaneneskpc.domain.repository

import com.kaaneneskpc.domain.model.TravelListing
import kotlinx.coroutines.flow.Flow

interface ListingRepository {
    fun getAllListings(): Flow<List<TravelListing>>
    fun getListingById(id: String): Flow<TravelListing?>
}