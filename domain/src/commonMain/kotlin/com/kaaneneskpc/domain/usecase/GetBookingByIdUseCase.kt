package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.Booking
import com.kaaneneskpc.domain.repository.BookingRepository

class GetBookingByIdUseCase(private val bookingRepository: BookingRepository) {
    suspend fun execute(id: String): Result<Booking> {
        return bookingRepository.getBookingById(id)
    }
}
