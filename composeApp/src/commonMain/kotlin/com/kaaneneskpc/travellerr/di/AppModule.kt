package com.kaaneneskpc.travellerr.di

import com.kaaneneskpc.data.di.dataModule
import com.kaaneneskpc.domain.di.domainModule
import com.kaaneneskpc.presentation.di.presentationModule
import com.kaaneneskpc.travellerr.payment.StripePaymentHandler
import org.koin.dsl.module

val stripeModule = module {
    single { StripePaymentHandler() }
}
val appModule = listOf(
    platformModule(), presentationModule, domainModule, dataModule, stripeModule
)

expect fun platformModule(): org.koin.core.module.Module