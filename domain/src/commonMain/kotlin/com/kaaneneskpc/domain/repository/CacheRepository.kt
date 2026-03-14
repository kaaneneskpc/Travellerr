package com.kaaneneskpc.domain.repository

import kotlinx.coroutines.flow.Flow

interface CacheRepository {
    suspend fun getAuthToken(): String?
    suspend fun clearAuthToken()
}