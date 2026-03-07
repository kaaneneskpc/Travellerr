package com.kaaneneskpc.domain.di

import com.kaaneneskpc.domain.repository.CacheRepository
import com.kaaneneskpc.domain.repository.ListingRepository
import com.kaaneneskpc.domain.repository.UserRepository
import com.kaaneneskpc.domain.usecase.GetAllListingUseCase
import com.kaaneneskpc.domain.usecase.GetAuthTokenUseCase
import com.kaaneneskpc.domain.usecase.RegisterUseCase
import com.kaaneneskpc.domain.usecase.SignInUseCase
import org.koin.dsl.module

val domainModule = module {
    factory {
        GetAllListingUseCase(get<ListingRepository>())
    }
    factory {
        SignInUseCase(get<UserRepository>())
    }
    factory {
        RegisterUseCase(get<UserRepository>())
    }
    factory {
        GetAuthTokenUseCase(get<CacheRepository>())
    }
}