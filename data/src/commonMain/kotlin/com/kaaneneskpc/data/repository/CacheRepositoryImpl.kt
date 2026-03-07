package com.kaaneneskpc.data.repository

import com.kaaneneskpc.data.dataSource.CacheDataSource
import com.kaaneneskpc.domain.repository.CacheRepository
import kotlinx.coroutines.flow.Flow

class CacheRepositoryImpl(private val cacheDataSource: CacheDataSource) : CacheRepository {
    override fun getAuthTokenFlow(): Flow<String?> {
        return cacheDataSource.getAuthTokenFlow()
    }

    override suspend fun saveAuthToken(token: String) {
        cacheDataSource.saveAuthToken(token)
    }
}