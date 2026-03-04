package com.kaaneneskpc.domain.repository

import com.kaaneneskpc.domain.model.RegisterModel
import com.kaaneneskpc.domain.model.UserModel


interface UserRepository {
    suspend fun login(email: String, password: String): Result<UserModel>
    suspend fun register(request: RegisterModel): Result<UserModel>
}