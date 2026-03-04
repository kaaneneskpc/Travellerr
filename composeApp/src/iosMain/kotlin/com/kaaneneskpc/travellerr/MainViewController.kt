package com.kaaneneskpc.travellerr

import androidx.compose.ui.window.ComposeUIViewController
import com.kaaneneskpc.travellerr.di.appModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(configure = {
    startKoin { modules(appModule) }
}) { App() }
