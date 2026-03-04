package com.kaaneneskpc.data.dataSource


import com.kaaneneskpc.data.model.request.RegisterRequest
import com.kaaneneskpc.data.model.request.SignInRequest
import com.kaaneneskpc.data.model.response.SignInResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class RemoteDataSource(private val httpClient: HttpClient, private val baseUrl: String) {
    private val BASE_URL = baseUrl
    private val SIGN_IN_ENDPOINT = "${BASE_URL}/auth/login"
    private val REGISTER_ENDPOINT = "${BASE_URL}/auth/register"

    suspend fun signIn(request: SignInRequest): Result<SignInResponse> {
        return try {
            val response = httpClient.post(urlString = SIGN_IN_ENDPOINT) {
                setBody(request)
            }
            Result.success(response.body())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
    suspend fun register(request: RegisterRequest): Result<SignInResponse> {
        return try {
            val response = httpClient.post(urlString = REGISTER_ENDPOINT) {
                setBody(request)
            }
            Result.success(response.body())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}