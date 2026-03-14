package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.Booking
import com.kaaneneskpc.domain.repository.BookingRepository

class UpdateBookingStatusUseCase(private val bookingRepository: BookingRepository) {
    suspend fun execute(id: String, status: String): Result<Booking> {
        return bookingRepository.updateBookingStatus(id, status)
    }
}
