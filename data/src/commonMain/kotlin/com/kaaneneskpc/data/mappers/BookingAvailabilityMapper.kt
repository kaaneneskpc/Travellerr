package com.kaaneneskpc.data.mappers

import com.kaaneneskpc.data.model.BookingAvailabilityDto
import com.kaaneneskpc.domain.model.BookingAvailability

object BookingAvailabilityMapper {

    fun toDomain(tripDate: BookingAvailabilityDto): BookingAvailability {

        return BookingAvailability(
            available = tripDate.available,
            reason = tripDate.reason,
            priceCalculation = tripDate.priceCalculation?.let { PriceCalculationMapper.toDomain(it) }
        )
    }

    fun toDomain(tripDates: List<BookingAvailabilityDto>): List<BookingAvailability> {
        return tripDates.map { toDomain(it) }
    }
}