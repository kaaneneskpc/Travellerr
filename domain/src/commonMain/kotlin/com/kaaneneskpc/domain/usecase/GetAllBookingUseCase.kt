package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.Booking
import com.kaaneneskpc.domain.repository.BookingRepository

class GetAllBookingUseCase(private val repository: BookingRepository) {
    suspend fun execute(): List<Booking>? {
        val data = repository.getBookings()
        return if (data.isSuccess) {
            data.getOrNull()
        } else {
            null
        }
    }
}