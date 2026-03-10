package com.kaaneneskpc.domain.repository

import com.kaaneneskpc.domain.model.TravelListing

interface ListingRepository {
    suspend fun getAllListings(): Result<List<TravelListing>>
    suspend fun getListingById(id: String): Result<TravelListing?>
}