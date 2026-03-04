package com.kaaneneskpc.travellerr.di

import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<String> { "http://localhost:8080" }
}