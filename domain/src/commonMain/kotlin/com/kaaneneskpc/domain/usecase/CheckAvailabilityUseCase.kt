package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.BookingAvailability
import com.kaaneneskpc.domain.repository.BookingRepository

class CheckAvailabilityUseCase(private val repository: BookingRepository) {
    suspend fun execute(
        listingId: String,
        tripDateId: String,
        noOfPeople: Int
    ): BookingAvailability? {
        val data = repository.checkAvailability(listingId, tripDateId, noOfPeople)
        return if (data.isSuccess) {
            data.getOrNull()
        } else {
            null
        }
    }
}