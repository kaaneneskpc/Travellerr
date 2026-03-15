package com.kaaneneskpc.travellerr.di

import android.content.Context
import com.kaaneneskpc.travellerr.cache.createDataStore
import com.kaaneneskpc.travellerr.cache.dataStoreFileName
import com.kaaneneskpc.travellerr.payment.StripePaymentHandler
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module  = module {
    single<StripePaymentHandler> { StripePaymentHandler() }
    single<String> { "http://10.0.2.2:8080" }
    single {
        createDataStore(
            producerPath = {
                get<Context>().filesDir.resolve(dataStoreFileName).absolutePath
            }
        )
    }
}