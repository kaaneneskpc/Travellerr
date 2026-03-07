package com.kaaneneskpc.travellerr

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.kaaneneskpc.travellerr.ui.details.TravellerrNavRoot
@Composable
@Preview
fun App() {
    MaterialTheme {
        TravellerrNavRoot()
    }
}