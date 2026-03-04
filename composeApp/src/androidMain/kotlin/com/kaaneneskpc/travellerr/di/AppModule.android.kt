package com.kaaneneskpc.travellerr.di

import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module  = module {
    single<String> { "http://10.0.2.2:8080" }
}