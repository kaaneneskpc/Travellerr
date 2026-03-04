package com.kaaneneskpc.data.di

import com.kaaneneskpc.data.dataSource.DummyDataSource
import com.kaaneneskpc.data.dataSource.RemoteDataSource
import com.kaaneneskpc.data.repository.ListingRepositoryImpl
import com.kaaneneskpc.data.repository.UserRepositoryImp
import com.kaaneneskpc.domain.repository.ListingRepository
import com.kaaneneskpc.domain.repository.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataModule = module {
    single { DummyDataSource() }

    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.DEFAULT
            }
            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
    }

    single { RemoteDataSource(httpClient = get<HttpClient>(), get()) }

    single<ListingRepository> {
        ListingRepositoryImpl(
            get<DummyDataSource>()
        )
    }

    single<UserRepository> {
        UserRepositoryImp(
            get<RemoteDataSource>()
        )
    }
}