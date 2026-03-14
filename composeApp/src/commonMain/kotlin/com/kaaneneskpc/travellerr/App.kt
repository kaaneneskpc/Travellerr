package com.kaaneneskpc.travellerr

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.kaaneneskpc.presentation.feature.app.AppViewModel
import com.kaaneneskpc.travellerr.navigation.TravellerrNavRoot
import com.kaaneneskpc.travellerr.payment.StripePaymentHandler
import org.koin.compose.viewmodel.koinViewModel
import org.koin.mp.KoinPlatform.getKoin

@Composable
@Preview
fun App(viewModel: AppViewModel = koinViewModel()) {
    MaterialTheme {
        val stripePaymentHandler = getKoin().get<StripePaymentHandler>()
        LaunchedEffect(true) {
            stripePaymentHandler.initialize("pk_test_51R7eG2PuYfx93Z7rqvNr4D34k0VlW9AxpcGq7k1GYS1rdMk7WscTnhMDwBO8JQwsnb5uA1PKzGdkxBNKrYsr2kS3007GxcoCN5")
        }
        val uiSource = viewModel.state.collectAsState()
        if (!uiSource.value.isLoading) {
            key(uiSource.value.authToken) {
                TravellerrNavRoot(uiSource.value.authToken)
            }
        } else {
            CircularProgressIndicator()
        }
    }
}