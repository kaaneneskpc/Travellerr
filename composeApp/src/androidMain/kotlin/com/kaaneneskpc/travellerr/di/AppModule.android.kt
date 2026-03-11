package com.kaaneneskpc.travellerr.di

import android.content.Context
import com.kaaneneskpc.data.dataSource.createDataStore
import com.kaaneneskpc.data.dataSource.dataStoreFileName
import com.kaaneneskpc.travellerr.payment.StripePaymentHandler
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module  = module {
    single<StripePaymentHandler> { StripePaymentHandler() }
    single<String> { "https://unemissive-sanford-unabidingly.ngrok-free.dev" }
    single {
        createDataStore(
            producerPath = {
                get<Context>().filesDir.resolve(dataStoreFileName).absolutePath
            }
        )
    }
}