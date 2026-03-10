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
            description = dto.description,
            price = dto.price,
            currency = dto.currency,
            category = dto.category,
            vendorId = dto.vendorId,
            city = dto.city,
            country = dto.country,
            capacity = dto.capacity,
            availableFrom = dto.availableFrom,
            availableTo = dto.availableTo,
            amenities = dto.amenities,
            reviewCount = dto.reviewCount,
            isActive = dto.isActive,
            tripDates = dto.tripDates?.let { TripDateMapper.toDomain(it) }
        )
    }

    fun toDomain(dtos: List<TravelListingDto>): List<TravelListing> {
        return dtos.map { toDomain(it) }
    }
}