package com.kaaneneskpc.data.repository

import com.kaaneneskpc.data.dataSource.RemoteDataSource
import com.kaaneneskpc.data.mappers.TravelListingMapper
import com.kaaneneskpc.domain.model.TravelListing
import com.kaaneneskpc.domain.repository.ListingRepository

class ListingRepositoryImpl(val dataSource: RemoteDataSource) : ListingRepository {

    override suspend fun getAllListings(): Result<List<TravelListing>> {
        val dtos = dataSource.getAllListing()
        if (dtos.isSuccess) {
            val listings = dtos.getOrNull()!!.listings
            val models = TravelListingMapper.toDomain(listings)
            return Result.success(models)
        } else {
            throw dtos.exceptionOrNull()!!
        }
    }

    override suspend fun getListingById(id: String): Result<TravelListing?> {
        val dtoResult = dataSource.getListingByID(id)
        if (dtoResult.isSuccess) {
            val dto = dtoResult.getOrNull()
            val model = dto?.let { TravelListingMapper.toDomain(it) }
            return Result.success(model)
        } else {
            throw dtoResult.exceptionOrNull()!!
        }
    }
}