package com.kaaneneskpc.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kaaneneskpc.data.dataSource.CacheDataSource
import com.kaaneneskpc.data.dataSource.RemoteDataSource
import com.kaaneneskpc.domain.session.SessionManager
import com.kaaneneskpc.data.repository.BookingRepositoryImpl
import com.kaaneneskpc.data.repository.CacheRepositoryImpl
import com.kaaneneskpc.data.repository.ListingRepositoryImpl
import com.kaaneneskpc.data.repository.PaymentRepositoryImpl
import com.kaaneneskpc.data.repository.UserRepositoryImp
import com.kaaneneskpc.domain.repository.BookingRepository
import com.kaaneneskpc.domain.repository.CacheRepository
import com.kaaneneskpc.domain.repository.ListingRepository
import com.kaaneneskpc.domain.repository.PaymentRepository
import com.kaaneneskpc.domain.repository.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataModule = module {
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
            HttpResponseValidator {
                validateResponse { response ->
                    val statusCode = response.status.value
                    if (statusCode == 401) {
                        val cacheDataSource = get<CacheDataSource>()
                        cacheDataSource.clearAuthToken()
                        SessionManager.emitSessionExpired()
                        val body = response.bodyAsText()
                        throw Exception("HTTP $statusCode: $body")
                    } else if (statusCode >= 400) {
                        val body = response.bodyAsText()
                        throw Exception("HTTP $statusCode: $body")
                    }
                }
            }
        }
    }

    single { RemoteDataSource(httpClient = get<HttpClient>(), get(), get()) }
    single { CacheDataSource(dataStore = get<DataStore<Preferences>>()) }

    single<ListingRepository> {
        ListingRepositoryImpl(
            get<RemoteDataSource>()
        )
    }

    single<UserRepository> {
        UserRepositoryImp(
            get<RemoteDataSource>(), get<CacheDataSource>()
        )
    }
    single<CacheRepository> {
        CacheRepositoryImpl(get())
    }

    single<BookingRepository> { BookingRepositoryImpl(get()) }
    single<PaymentRepository> { PaymentRepositoryImpl(get()) }
}