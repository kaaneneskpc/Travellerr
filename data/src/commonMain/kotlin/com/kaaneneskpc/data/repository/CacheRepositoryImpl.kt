package com.kaaneneskpc.data.repository

import com.kaaneneskpc.data.dataSource.CacheDataSource
import com.kaaneneskpc.domain.repository.CacheRepository

class CacheRepositoryImpl(private val cacheDataSource: CacheDataSource) : CacheRepository {
    override suspend fun getAuthToken(): String? {
        return cacheDataSource.getAuthToken()
    }
}