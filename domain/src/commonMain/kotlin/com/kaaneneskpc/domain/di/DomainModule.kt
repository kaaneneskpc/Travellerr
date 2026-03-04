package com.kaaneneskpc.domain.di

import com.kaaneneskpc.domain.repository.ListingRepository
import com.kaaneneskpc.domain.usecase.GetAllListingUseCase
import org.koin.dsl.module

val domainModule = module {
    factory {
        GetAllListingUseCase(get<ListingRepository>())
    }
}