package com.kaaneneskpc.travellerr

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.kaaneneskpc.presentation.feature.app.AppViewModel
import com.kaaneneskpc.travellerr.navigation.TravellerrNavRoot
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(viewModel: AppViewModel = koinViewModel()) {
    MaterialTheme {
        val uiSource = viewModel.state.collectAsState()
        if (!uiSource.value.isLoading) {
            TravellerrNavRoot(uiSource.value.authToken)
        } else {
            CircularProgressIndicator()
        }
    }
}