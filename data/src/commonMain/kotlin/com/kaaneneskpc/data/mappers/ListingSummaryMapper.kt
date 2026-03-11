package com.kaaneneskpc.data.mappers

import com.kaaneneskpc.data.model.ListingSummaryDto
import com.kaaneneskpc.domain.model.ListingSummary

object ListingSummaryMapper {
    fun toDomain(tripDate: ListingSummaryDto): ListingSummary {

        return ListingSummary(
            images = tripDate.images,
            location = tripDate.location,
            title = tripDate.title
        )
    }

    fun toDomain(tripDates: List<ListingSummaryDto>): List<ListingSummary> {
        return tripDates.map { toDomain(it) }
    }
}