package com.kaaneneskpc.data.repository

import com.kaaneneskpc.data.dataSource.RemoteDataSource
import com.kaaneneskpc.data.mappers.BookingMapper
import com.kaaneneskpc.data.mappers.PriceCalculationMapper
import com.kaaneneskpc.data.model.request.BookingInfoRequest
import com.kaaneneskpc.domain.model.Booking
import com.kaaneneskpc.domain.model.BookingAvailability
import com.kaaneneskpc.domain.repository.BookingRepository

class BookingRepositoryImpl(private val remoteDataSource: RemoteDataSource) : BookingRepository {
    override suspend fun checkAvailability(
        listingId: String,
        tripDateID: String,
        checkInDate: String,
        checkOutDate: String,
        numberOfGuests: Int
    ): Result<BookingAvailability> {

        val result = remoteDataSource.checkBookingAvailability(
            BookingInfoRequest(
                listingId = listingId,
                tripDateId = tripDateID,
                checkInDate = checkInDate,
                checkOutDate = checkOutDate,
                numberOfGuests = numberOfGuests
            )
        )
        if (result.isSuccess) {
            val response = result.getOrNull()!!
            return Result.success(
                BookingAvailability(
                    available = response.available,
                    priceCalculation = PriceCalculationMapper.toDomain(response.priceCalculation),
                    reason = response.reason
                )
            )
        } else {
            return Result.failure(result.exceptionOrNull()!!)
        }

    }

    override suspend fun createBooking(
        listingId: String,
        tripDateID: String,
        checkInDate: String,
        checkOutDate: String,
        numberOfGuests: Int,
        specialRequests: String?
    ): Result<Booking> {

        val result = remoteDataSource.createBooking(
            BookingInfoRequest(
                listingId = listingId,
                tripDateId = tripDateID,
                checkInDate = checkInDate,
                checkOutDate = checkOutDate,
                numberOfGuests = numberOfGuests,
                specialRequests = specialRequests
            ))

        if (result.isSuccess) {
            val response = result.getOrNull()!!
            return Result.success(
                BookingMapper.toDomain(response)
            )
        } else {
            return Result.failure(result.exceptionOrNull()!!)
        }
    }

    override suspend fun getBookings(): Result<List<Booking>> {
        val result = remoteDataSource.getAllBookings()
        if (result.isSuccess) {
            val response = result.getOrNull()!!
            return Result.success(
                BookingMapper.toDomain(response)
            )
        } else {
            return Result.failure(result.exceptionOrNull()!!)
        }
    }
}