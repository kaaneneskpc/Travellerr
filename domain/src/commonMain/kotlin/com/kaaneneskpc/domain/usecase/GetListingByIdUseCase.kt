package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.TravelListing
import com.kaaneneskpc.domain.repository.ListingRepository

class GetListingByIdUseCase(private val repository: ListingRepository) {
    suspend fun execute(id: String): TravelListing? {
        val data = repository.getListingById(id)
        if (data.isSuccess) {
            return data.getOrNull()
        } else {
            return null
        }
    }
}