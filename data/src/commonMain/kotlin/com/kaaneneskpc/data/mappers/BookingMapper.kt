package com.kaaneneskpc.data.mappers

import com.kaaneneskpc.data.model.BookingDto
import com.kaaneneskpc.domain.model.Booking

object BookingMapper {
    fun toDomain(tripDate: BookingDto): Booking {

        return Booking(
            id = tripDate.id,
            listingId = tripDate.listingId,
            tripDateId = tripDate.tripDateId,
            numberOfGuests = tripDate.numberOfGuests,
            totalPrice = tripDate.totalPrice,
            currency = tripDate.currency,
            createdAt = tripDate.createdAt,
            updatedAt = tripDate.updatedAt,
            checkInDate = tripDate.checkInDate,
            checkOutDate = tripDate.checkOutDate,
            paymentId = tripDate.paymentId,
            paymentStatus = tripDate.paymentStatus,
            specialRequests = tripDate.specialRequests,
            status = tripDate.status,
            customerId = tripDate.customerId,
            listing = tripDate.listing?.let { ListingSummaryMapper.toDomain(it) },
            tripDate = tripDate.tripDate?.let { TripDateSummaryMapper.toDomain(it) }
        )
    }

    fun toDomain(tripDates: List<BookingDto>): List<Booking> {
        return tripDates.map { toDomain(it) }
    }
}