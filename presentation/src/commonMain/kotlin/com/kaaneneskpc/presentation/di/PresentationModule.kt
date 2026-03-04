package com.kaaneneskpc.presentation.di

import com.kaaneneskpc.domain.usecase.GetAllListingUseCase
import com.kaaneneskpc.presentation.listings.TravelListingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val presentationModule = module {
    viewModel { TravelListingViewModel(get<GetAllListingUseCase>()) }
}