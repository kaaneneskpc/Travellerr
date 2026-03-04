package com.kaaneneskpc.domain.usecase

import com.kaaneneskpc.domain.model.UserModel
import com.kaaneneskpc.domain.repository.UserRepository

class SignInUseCase(private val repository: UserRepository) {
    suspend fun execute(userName: String, password: String): Result<UserModel> {
        return repository.login(userName, password)
    }
}