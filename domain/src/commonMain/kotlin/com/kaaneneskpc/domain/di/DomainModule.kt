package com.kaaneneskpc.domain.di

import com.kaaneneskpc.domain.repository.BookingRepository
import com.kaaneneskpc.domain.repository.CacheRepository
import com.kaaneneskpc.domain.repository.ListingRepository
import com.kaaneneskpc.domain.repository.PaymentRepository
import com.kaaneneskpc.domain.repository.UserRepository
import com.kaaneneskpc.domain.usecase.CheckAvailabilityUseCase
import com.kaaneneskpc.domain.usecase.CreateBookingUseCase
import com.kaaneneskpc.domain.usecase.CreatePaymentIntentUseCase
import com.kaaneneskpc.domain.usecase.GetAllBookingUseCase
import com.kaaneneskpc.domain.usecase.GetAllListingUseCase
import com.kaaneneskpc.domain.usecase.GetAuthTokenUseCase
import com.kaaneneskpc.domain.usecase.GetListingByIdUseCase
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
    factory {
        GetListingByIdUseCase(get<ListingRepository>())
    }
    factory {
        CheckAvailabilityUseCase(get<BookingRepository>())
    }
    factory {
        CreateBookingUseCase(get<BookingRepository>())
    }
    factory {
        CreatePaymentIntentUseCase(get<PaymentRepository>())
    }
    factory {
        GetAllBookingUseCase(get<BookingRepository>())
    }
    factory {
        com.kaaneneskpc.domain.usecase.GetBookingByIdUseCase(get<BookingRepository>())
    }
}