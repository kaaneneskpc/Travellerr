package com.kaaneneskpc.domain.repository

import kotlinx.coroutines.flow.Flow

interface CacheRepository {
    fun getAuthTokenFlow(): Flow<String?>
    suspend fun saveAuthToken(token: String)
}