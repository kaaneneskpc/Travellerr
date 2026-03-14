package com.kaaneneskpc.travellerr.ui.bookings

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import coil3.compose.AsyncImage
import com.kaaneneskpc.domain.model.Booking
import com.kaaneneskpc.domain.model.TravelListing
import com.kaaneneskpc.presentation.feature.bookings.BookingListViewModel
import com.kaaneneskpc.travellerr.navigation.NavRoutes
import org.koin.compose.viewmodel.koinViewModel

val SlantedShape = GenericShape { size, _ ->
    val slant = size.width * 0.15f
    moveTo(slant, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width - slant, size.height)
    lineTo(0f, size.height)
    close()
}

@Composable
fun AnimatedAbstractBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = Modifier.fillMaxSize().background(Color(0xFFE8F6F3))) {
        val width = size.width
        val height = size.height
        translate(
            left = (rotation / 360f) * width * 0.1f,
            top = (rotation / 360f) * height * 0.1f
        ) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x8896E6D0), Color.Transparent),
                    center = Offset(width * 0.2f, height * 0.2f),
                    radius = width * 0.7f * pulse
                ),
                size = size
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x88C5CAE9), Color.Transparent),
                    center = Offset(width * 0.8f, height * 0.8f),
                    radius = width * 0.6f / pulse
                ),
                size = size
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x66FFCCBC), Color.Transparent),
                    center = Offset(width * 0.5f, height * 0.5f),
                    radius = width * 0.4f * pulse
                ),
                size = size
            )
        }
    }
}

@Composable
fun BookingListScreen(
    backStack: NavBackStack<NavKey>,
    viewModel: BookingListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedAbstractBackground()

        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(56.dp))
            Text(
                text = "My Journeys",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B3A36)
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

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
            } else if (uiState.bookings.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No journeys mapped yet.", color = Color(0xFF5D7A74))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    items(uiState.bookings, key = { it.id }) { booking ->
                        val listing = uiState.listingsMap[booking.listingId]
                        GlassBookingListItem(
                            booking = booking,
                            listing = listing,
                            onClick = { backStack.add(NavRoutes.BookingDetail(booking.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GlowingStatusDot(status: String) {
    val (baseColor, glowColor) = when (status) {
        "CONFIRMED" -> Color(0xFF00FF88) to Color(0x6600FF88)
        "PENDING" -> Color(0xFFFFB800) to Color(0x66FFB800)
        "CANCELLED" -> Color(0xFFFF3366) to Color(0x66FF3366)
        else -> Color.Gray to Color(0x66888888)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val radiusAnim by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "radius"
    )

    Canvas(modifier = Modifier.size(16.dp)) {
        val centerOffset = Offset(size.width / 2, size.height / 2)
        drawCircle(
            color = glowColor,
            radius = radiusAnim.dp.toPx(),
            center = centerOffset
        )
        drawCircle(
            color = baseColor,
            radius = 3.dp.toPx(),
            center = centerOffset
        )
    }
}

@Composable
fun GlassBookingListItem(
    booking: Booking,
    listing: TravelListing?,
    onClick: () -> Unit = {}
) {
    val title = listing?.title ?: booking.listing?.title ?: "Booking #${booking.id.take(8)}"
    val checkIn = booking.checkInDate?.split("T")?.firstOrNull() ?: "N/A"
    val checkOut = booking.checkOutDate?.split("T")?.firstOrNull() ?: "N/A"
    val imageUrl = listing?.images?.firstOrNull() ?: booking.listing?.images?.firstOrNull()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xBBFFFFFF))
            .clickable { onClick() }
            .border(
                BorderStroke(1.dp, Brush.linearGradient(
                    colors = listOf(Color(0x44B2DFDB), Color(0x22B2DFDB))
                )),
                RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Listing Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(SlantedShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(SlantedShape)
                        .background(Color(0x33B2DFDB)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Placeholder",
                        modifier = Modifier.size(40.dp),
                        tint = Color(0xFF4DB6AC)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1B3A36)
                    ),
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$checkIn  →  $checkOut",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF5D7A74))
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        GlowingStatusDot(booking.status)
                        Text(
                            text = booking.status,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF37474F),
                                letterSpacing = 1.sp
                            )
                        )
                    }

                    Text(
                        text = "${booking.totalPrice} ${booking.currency}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00897B)
                        )
                    )
                }
            }
        }
    }
}