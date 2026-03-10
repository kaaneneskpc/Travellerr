package com.kaaneneskpc.domain.repository

import com.kaaneneskpc.domain.model.Booking
import com.kaaneneskpc.domain.model.BookingAvailability


interface BookingRepository {
    suspend fun checkAvailability(
        listingId: String,
        tripDateID: String,
        numberOfGuests: Int
    ): Result<BookingAvailability>

    suspend fun createBooking(
        listingId: String,
        tripDateID: String,
        numberOfGuests: Int,
        specialRequests: String? = null
    ): Result<Booking>
}