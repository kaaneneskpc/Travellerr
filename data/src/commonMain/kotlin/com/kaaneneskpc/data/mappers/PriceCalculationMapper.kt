package com.kaaneneskpc.data.mappers

import com.kaaneneskpc.data.model.PriceCalculationDto
import com.kaaneneskpc.domain.model.PriceCalculation


object PriceCalculationMapper {

    fun toDomain(tripDate: PriceCalculationDto?): PriceCalculation? {
        if (tripDate == null)
            return null
        return PriceCalculation(
            subtotal = tripDate.subtotal,
            taxes = tripDate.taxes,
            serviceFee = tripDate.serviceFee,
            total = tripDate.total,
            currency = tripDate.currency,
            numberOfNights = tripDate.numberOfNights,
            numberOfGuests = tripDate.numberOfGuests
        )
    }

    fun toDomain(tripDates: List<PriceCalculationDto>): List<PriceCalculation> {
        return tripDates.map { toDomain(it)!! }
    }
}