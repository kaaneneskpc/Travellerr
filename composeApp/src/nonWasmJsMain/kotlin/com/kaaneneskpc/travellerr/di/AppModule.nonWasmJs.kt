package com.kaaneneskpc.travellerr.di

import com.kaaneneskpc.data.dataSource.CacheDataSource
import com.kaaneneskpc.travellerr.cache.DataStoreCacheSource
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun cacheModule(): Module = module {

    single<CacheDataSource> {
        DataStoreCacheSource(get())
    }

}