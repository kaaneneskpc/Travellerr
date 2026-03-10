package com.kaaneneskpc.data.mappers

import com.kaaneneskpc.data.model.TripDateDto
import com.kaaneneskpc.domain.model.TripDate

object TripDateMapper {

    fun toDomain(tripDate: TripDateDto): TripDate {

        return TripDate(
            availableSpots = tripDate.availableSpots,
            createdAt = tripDate.createdAt,
            currentBookings = tripDate.currentBookings,
            endDate = tripDate.endDate,
            id = tripDate.id,
            isActive = tripDate.isActive,
            listingId = tripDate.listingId,
            maxCapacity = tripDate.maxCapacity,
            startDate = tripDate.startDate,
            updatedAt = tripDate.updatedAt
        )
    }

    fun toDomain(tripDates: List<TripDateDto>): List<TripDate> {
        return tripDates.map { toDomain(it) }
    }
}