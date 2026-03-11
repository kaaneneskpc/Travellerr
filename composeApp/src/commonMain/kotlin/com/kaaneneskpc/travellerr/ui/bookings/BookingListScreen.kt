package com.kaaneneskpc.travellerr.ui.bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import coil3.compose.AsyncImage
import com.kaaneneskpc.domain.model.Booking
import com.kaaneneskpc.domain.model.TravelListing
import com.kaaneneskpc.presentation.feature.bookings.BookingListViewModel
import com.kaaneneskpc.travellerr.widgets.TravellerrSpacer
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun BookingListScreen(
    backStack: NavBackStack<NavKey>,
    viewModel: BookingListViewModel = koinViewModel()
) {
    Scaffold {
        val uiState = viewModel.uiState.collectAsState()
        if (uiState.value.isLoading) {
            Column(modifier = Modifier.padding(it).fillMaxSize()) {
                CircularProgressIndicator()
                Text(text = "Loading bookings...")
            }
        } else if (uiState.value.errorMessage != null) {
            Column(modifier = Modifier.padding(it).fillMaxSize()) {
                Text(text = "Error: ${uiState.value.errorMessage}")
            }
        } else {
            if (uiState.value.bookings.isEmpty()) {
                Column(modifier = Modifier.padding(it).fillMaxSize()) {
                    Text(text = "No bookings found.")
                }
            } else {
                LazyColumn(modifier = Modifier.padding(it).fillMaxSize().padding(16.dp)) {
                    items(uiState.value.bookings) { booking ->
                        val listing = uiState.value.listingsMap[booking.listingId]
                        BookingListItem(booking = booking, listing = listing)
                    }
                }
            }
        }
    }
}

@Composable
fun BookingListItem(booking: Booking, listing: TravelListing?) {
    val title = listing?.title ?: booking.listing?.title ?: "Booking #${booking.id.take(8)}"
    val checkIn = booking.checkInDate?.split("T")?.firstOrNull() ?: "N/A"
    val checkOut = booking.checkOutDate?.split("T")?.firstOrNull() ?: "N/A"
    val imageUrl = listing?.images?.firstOrNull() ?: booking.listing?.images?.firstOrNull()
    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Listing Image",
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Placeholder",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            TravellerrSpacer(16.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$checkIn - $checkOut",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Status: ${booking.status}",
                    style = MaterialTheme.typography.bodySmall,
                    color = when (booking.status) {
                        "CONFIRMED" -> MaterialTheme.colorScheme.primary
                        "PENDING" -> MaterialTheme.colorScheme.tertiary
                        "CANCELLED" -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
                Text(
                    text = "${booking.totalPrice} ${booking.currency}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}