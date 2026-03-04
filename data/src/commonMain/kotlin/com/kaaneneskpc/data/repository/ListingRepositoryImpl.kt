package com.kaaneneskpc.data.repository

import com.kaaneneskpc.data.dataSource.DummyDataSource
import com.kaaneneskpc.data.mappers.TravelListingMapper
import com.kaaneneskpc.domain.model.TravelListing
import com.kaaneneskpc.domain.repository.ListingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ListingRepositoryImpl(val dataSource: DummyDataSource) : ListingRepository {

    private val _listings = MutableStateFlow<List<TravelListing>>(emptyList())
    val listings: StateFlow<List<TravelListing>> = _listings.asStateFlow()

    override fun getAllListings(): Flow<List<TravelListing>> {
        return dataSource.listings.map {
            val domainModels = TravelListingMapper.toDomain(it)
            _listings.value = domainModels
            listings.value
        }
    }

    override fun getListingById(id: String): Flow<TravelListing?> {
        return listings.map { list ->
            list.find { it.id == id }
        }
    }
}