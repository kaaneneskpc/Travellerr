package com.kaaneneskpc.travellerr.di

import com.kaaneneskpc.data.di.dataModule
import com.kaaneneskpc.domain.di.domainModule
import com.kaaneneskpc.presentation.di.presentationModule


val appModule = listOf(
    presentationModule, domainModule, dataModule
)