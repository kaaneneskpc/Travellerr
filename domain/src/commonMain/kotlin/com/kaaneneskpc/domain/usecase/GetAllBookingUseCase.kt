package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.Booking
import com.kaaneneskpc.domain.repository.BookingRepository

class GetAllBookingUseCase(private val repository: BookingRepository) {
    suspend fun execute(): Result<List<Booking>> {
        return repository.getBookings()
    }
}