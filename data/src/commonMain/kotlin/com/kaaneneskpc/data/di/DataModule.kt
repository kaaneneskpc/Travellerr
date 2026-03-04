package com.kaaneneskpc.data.di

import com.kaaneneskpc.data.dataSource.DummyDataSource
import com.kaaneneskpc.data.repository.ListingRepositoryImpl
import com.kaaneneskpc.domain.repository.ListingRepository
import org.koin.dsl.module

val dataModule = module {
    single { DummyDataSource() }

    single<ListingRepository> {
        ListingRepositoryImpl(
            get<DummyDataSource>()
        )
    }
}