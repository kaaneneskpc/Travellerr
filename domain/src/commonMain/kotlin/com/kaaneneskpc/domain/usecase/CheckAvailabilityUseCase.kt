package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.BookingAvailability
import com.kaaneneskpc.domain.repository.BookingRepository

class CheckAvailabilityUseCase(private val repository: BookingRepository) {
    suspend fun execute(
        listingId: String,
        tripDateId: String,
        checkInDate: String,
        checkOutDate: String,
        noOfPeople: Int
    ): Result<BookingAvailability> {
        return repository.checkAvailability(listingId, tripDateId, checkInDate, checkOutDate, noOfPeople)
    }
}