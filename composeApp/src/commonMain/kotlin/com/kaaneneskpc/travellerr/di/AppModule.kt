package com.kaaneneskpc.travellerr.di

import com.kaaneneskpc.data.di.dataModule
import com.kaaneneskpc.domain.di.domainModule
import com.kaaneneskpc.presentation.di.presentationModule


val appModule = listOf(
    platformModule(), presentationModule, domainModule, dataModule
)

expect fun platformModule(): org.koin.core.module.Module