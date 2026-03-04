package com.kaaneneskpc.data.repository

import com.kaaneneskpc.data.dataSource.RemoteDataSource
import com.kaaneneskpc.data.mappers.RegisterRequestMapper
import com.kaaneneskpc.data.mappers.UserMapper
import com.kaaneneskpc.data.model.request.SignInRequest
import com.kaaneneskpc.domain.model.RegisterModel
import com.kaaneneskpc.domain.model.UserModel
import com.kaaneneskpc.domain.repository.UserRepository

class UserRepositoryImp(val dataSource: RemoteDataSource) : UserRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Result<UserModel> {

        return try {
            val response = dataSource.signIn(SignInRequest(email, password))
            if (response.isSuccess) {
                val response = response.getOrNull()!!
                val userModel = UserMapper.toDomain(response.user)
                Result.success(userModel)
            } else {
                Result.failure(Exception("Login failed with status code: ${response.exceptionOrNull()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterModel): Result<UserModel> {
        return try {
            val response = dataSource.register(RegisterRequestMapper.toDto(request))
            if (response.isSuccess) {
                val response = response.getOrNull()!!
                val userModel = UserMapper.toDomain(response.user)
                Result.success(userModel)
            } else {
                Result.failure(Exception("Registration failed with status code: ${response.exceptionOrNull()}"))
            }

        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}