package com.kaaneneskpc.domain.model

data class ListingSummary(
    val images: List<String>,
    val location: String,
    val title: String
)