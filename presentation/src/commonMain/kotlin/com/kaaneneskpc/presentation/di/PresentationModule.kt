package com.kaaneneskpc.presentation.di

import com.kaaneneskpc.domain.usecase.GetAllListingUseCase
import com.kaaneneskpc.domain.usecase.RegisterUseCase
import com.kaaneneskpc.domain.usecase.SignInUseCase
import com.kaaneneskpc.presentation.feature.listings.TravelListingViewModel
import com.kaaneneskpc.presentation.feature.register.RegisterViewModel
import com.kaaneneskpc.presentation.feature.signIn.SignInViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val presentationModule = module {
    viewModel { TravelListingViewModel(get<GetAllListingUseCase>()) }
    viewModel { SignInViewModel(get<SignInUseCase>()) }
    viewModel { RegisterViewModel(get<RegisterUseCase>()) }
}