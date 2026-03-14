package com.kaaneneskpc.travellerr.ui.bookings.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import coil3.compose.AsyncImage
import com.kaaneneskpc.domain.model.Booking
import com.kaaneneskpc.domain.model.TravelListing
import com.kaaneneskpc.presentation.feature.bookings.details.BookingDetailViewModel
import com.kaaneneskpc.travellerr.navigation.NavRoutes
import com.kaaneneskpc.travellerr.ui.bookings.AnimatedAbstractBackground
import com.kaaneneskpc.travellerr.ui.bookings.GlowingStatusDot
import com.kaaneneskpc.travellerr.ui.bookings.SlantedShape
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookingDetailScreen(
    backStack: NavBackStack<NavKey>,
    bookingId: String,
    viewModel: BookingDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(bookingId) {
        viewModel.fetchBookingDetails(bookingId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedAbstractBackground() // Reuse the fresh mint background

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF4DB6AC))
            }
        } else if (uiState.errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Error: ${uiState.errorMessage}",
                    color = Color(0xFFD32F2F)
                )
            }
        } else if (uiState.booking != null) {
            val booking = uiState.booking!!
            val listing = uiState.listing
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Header Image
                DetailHeaderSection(
                    booking = booking,
                    listing = listing,
                    onBackClick = { backStack.add(NavRoutes.BookingList) }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Content Information in Glassmorphism wrappers
                Column(modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 40.dp)) {
                    DetailGlassInfoBox(booking, listing)
                }
            }
        }
    }
}

@Composable
fun DetailHeaderSection(booking: Booking, listing: TravelListing?, onBackClick: () -> Unit) {
    val imageUrl = listing?.images?.firstOrNull() ?: booking.listing?.images?.firstOrNull()

    Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Listing Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(com.kaaneneskpc.travellerr.ui.bookings.SlantedShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(com.kaaneneskpc.travellerr.ui.bookings.SlantedShape)
                    .background(Color(0x33B2DFDB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Placeholder",
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFF4DB6AC)
                )
            }
        }

        // Back button over glass
        Box(
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xBBFFFFFF))
                .border(
                    BorderStroke(1.dp, Brush.linearGradient(listOf(Color(0x44B2DFDB), Color(0x22B2DFDB)))),
                    RoundedCornerShape(16.dp)
                )
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Color(0xFF1B3A36),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun DetailGlassInfoBox(booking: Booking, listing: TravelListing?) {
    val title = listing?.title ?: booking.listing?.title ?: "Booking #${booking.id.take(8)}"
    val checkIn = booking.checkInDate?.split("T")?.firstOrNull() ?: "N/A"
    val checkOut = booking.checkOutDate?.split("T")?.firstOrNull() ?: "N/A"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xCCFFFFFF))
            .border(
                BorderStroke(1.dp, Brush.linearGradient(
                    colors = listOf(Color(0x66B2DFDB), Color(0x33B2DFDB))
                )),
                RoundedCornerShape(24.dp)
            )
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1B3A36)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Status and Payment
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    com.kaaneneskpc.travellerr.ui.bookings.GlowingStatusDot(booking.status)
                    Text(
                        text = "Status: ${booking.status}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF37474F)
                        )
                    )
                }
                
                Text(
                    text = "Payment: ${booking.paymentStatus}",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF00897B)
                    )
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0x221B3A36))

            // Dates and Guests
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("CHECK-IN", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF5D7A74)))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(checkIn, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1B3A36)))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("CHECK-OUT", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF5D7A74)))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(checkOut, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1B3A36)))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Guests: ${booking.numberOfGuests}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF37474F))
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0x221B3A36))

            // Price Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Price",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF5D7A74)
                    )
                )
                Text(
                    text = "${booking.totalPrice} ${booking.currency}",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF00897B)
                    )
                )
            }
            
            if (!booking.paymentId.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Ref: ${booking.paymentId}",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
