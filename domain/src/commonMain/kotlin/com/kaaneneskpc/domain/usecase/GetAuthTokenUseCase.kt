package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.repository.CacheRepository
import kotlinx.coroutines.flow.Flow

class GetAuthTokenUseCase(private val repository: CacheRepository) {
    fun execute(): Flow<String?> {
        return repository.getAuthTokenFlow()
    }
}