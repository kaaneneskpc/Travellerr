package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.repository.BookingRepository

class DeleteBookingUseCase(private val bookingRepository: BookingRepository) {
    suspend fun execute(id: String): Result<Unit> {
        return bookingRepository.deleteBooking(id)
    }
}
