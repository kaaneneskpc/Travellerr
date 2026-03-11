package com.kaaneneskpc.travellerr.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kaaneneskpc.data.dataSource.createDataStore
import com.kaaneneskpc.data.dataSource.dataStoreFileName
import com.kaaneneskpc.travellerr.payment.StripePaymentHandler
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual fun platformModule(): Module = module {
    single<StripePaymentHandler> { StripePaymentHandler() }
    single<String> { "https://unemissive-sanford-unabidingly.ngrok-free.dev" }
    single <DataStore<Preferences>>{
        createDataStore(producerPath = { "${getDocumentPath()}/${dataStoreFileName}" })
    }
}


@OptIn(ExperimentalForeignApi::class)
fun getDocumentPath(): String {
    val documentPath = NSFileManager.defaultManager.URLForDirectory(
        directory =  NSDocumentDirectory,
        inDomain =  NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    return requireNotNull(documentPath!!.path)
}