package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.TravelListing
import com.kaaneneskpc.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GetAllListingUseCase(private val repository: ListingRepository) {
    suspend fun execute(): List<TravelListing> {
        val data = repository.getAllListings()
        if (data.isSuccess) {
            return data.getOrNull()!!
        } else {
            return emptyList()
        }
    }
}