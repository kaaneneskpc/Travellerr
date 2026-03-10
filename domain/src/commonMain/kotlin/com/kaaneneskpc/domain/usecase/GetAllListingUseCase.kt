package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.TravelListing
import com.kaaneneskpc.domain.repository.ListingRepository


class GetAllListingUseCase(private val repository: ListingRepository) {
    suspend fun execute(): List<TravelListing> {
        val data = repository.getAllListings()
        return if (data.isSuccess) {
            data.getOrNull()!!
        } else {
            emptyList()
        }
    }
}