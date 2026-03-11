package com.kaaneneskpc.data.mappers

import com.kaaneneskpc.data.model.TripDateSummaryDto
import com.kaaneneskpc.domain.model.TripDateSummary

object TripDateSummaryMapper {
    fun toDomain(tripDate: TripDateSummaryDto): TripDateSummary {

        return TripDateSummary(
            endDate = tripDate.endDate,
            startDate = tripDate.startDate
        )
    }

    fun toDomain(tripDates: List<TripDateSummaryDto>): List<TripDateSummary> {
        return tripDates.map { toDomain(it) }
    }
}