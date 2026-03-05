package com.kaaneneskpc.data.mappers

import com.kaaneneskpc.data.model.TravelListingDto
import com.kaaneneskpc.domain.model.TravelListing

object TravelListingMapper {

    fun toDomain(dto: TravelListingDto): TravelListing {
        return TravelListing(
            id = dto.id,
            title = dto.title,
            location = dto.location,
            images = dto.images,
            rating = dto.rating,
        )
    }

    fun toDomain(dtos: List<TravelListingDto>): List<TravelListing> {
        return dtos.map { toDomain(it) }
    }
}