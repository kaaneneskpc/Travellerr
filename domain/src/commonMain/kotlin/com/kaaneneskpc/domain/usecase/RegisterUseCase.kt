package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.RegisterModel
import com.kaaneneskpc.domain.model.UserModel
import com.kaaneneskpc.domain.repository.UserRepository

class RegisterUseCase(private val repository: UserRepository) {
    suspend fun execute(request: RegisterModel): Result<UserModel> {
        return repository.register(request)
    }
}