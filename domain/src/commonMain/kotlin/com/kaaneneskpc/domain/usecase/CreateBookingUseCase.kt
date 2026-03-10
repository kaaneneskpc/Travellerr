package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.Booking
import com.kaaneneskpc.domain.repository.BookingRepository

class CreateBookingUseCase(private val repository: BookingRepository) {
    suspend fun execute(
        listingId: String,
        tripDateId: String,
        checkInDate: String,
        checkOutDate: String,
        noOfPeople: Int
    ): Booking? {
        val data = repository.createBooking(listingId, tripDateId, checkInDate, checkOutDate, noOfPeople)
        return if (data.isSuccess) {
            data.getOrNull()
        } else {
            null
        }
    }
}