package com.kaaneneskpc.data.repository

import com.kaaneneskpc.data.dataSource.RemoteDataSource
import com.kaaneneskpc.data.mappers.PaymentIntentMapper
import com.kaaneneskpc.data.model.request.PaymentIntentInfoRequest
import com.kaaneneskpc.domain.model.PaymentIntent
import com.kaaneneskpc.domain.repository.PaymentRepository

class PaymentRepositoryImpl(val remoteDataSource: RemoteDataSource) : PaymentRepository {
    override suspend fun createPaymentIntent(
        bookingId: String,
        amount: Double?,
        currency: String
    ): Result<PaymentIntent> {
        val result = remoteDataSource.createPaymentIntent(
            PaymentIntentInfoRequest(
                bookingId = bookingId
            )
        )
        if (result.isSuccess) {
            val response = result.getOrNull()!!
            return Result.success(
                PaymentIntentMapper.toDomain(response)
            )
        } else {
            return Result.failure(result.exceptionOrNull()!!)
        }
    }

}