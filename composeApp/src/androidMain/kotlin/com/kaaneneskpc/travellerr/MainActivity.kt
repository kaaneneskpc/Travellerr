package com.kaaneneskpc.travellerr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kaaneneskpc.travellerr.di.appModule
import com.kaaneneskpc.travellerr.payment.PaymentSheetHolder
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        PaymentSheetHolder.instance = PaymentSheetHolder(this)
        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(appModule)
        }
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}