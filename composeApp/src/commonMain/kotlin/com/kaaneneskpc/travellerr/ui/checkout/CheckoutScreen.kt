package com.kaaneneskpc.travellerr.ui.checkout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import coil3.compose.AsyncImage
import com.kaaneneskpc.domain.model.BookingAvailability
import com.kaaneneskpc.domain.model.TravelListing
import com.kaaneneskpc.domain.model.TripDate
import com.kaaneneskpc.presentation.feature.checkout.CheckoutViewModel
import com.kaaneneskpc.travellerr.navigation.NavRoutes
import com.kaaneneskpc.travellerr.payment.PaymentResult
import com.kaaneneskpc.travellerr.payment.StripePaymentHandler
import com.kaaneneskpc.travellerr.ui.bookings.AnimatedAbstractBackground
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CheckoutScreen(
    backStack: NavBackStack<NavKey>,
    itemId: String,
    viewModel: CheckoutViewModel = koinViewModel { parametersOf(itemId) }
) {
    val shouldShowErrorDialog = remember { mutableStateOf(false) }
    val paymentHandler = remember { StripePaymentHandler() }
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.value.paymentIntent) {
        uiState.value.paymentIntent?.let {
            val resultStatus = paymentHandler.processPayment(it.clientSecret)
            when (resultStatus) {
                is PaymentResult.Success -> {
                    backStack.removeAll(
                        listOf(
                            NavRoutes.Checkout(itemId),
                            NavRoutes.ListingDetails(itemId)
                        )
                    )
                    backStack.add(NavRoutes.BookingList)
                }
                is PaymentResult.Failure -> {
                    shouldShowErrorDialog.value = true
                }
                is PaymentResult.Cancelled -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedAbstractBackground()

        if (shouldShowErrorDialog.value) {
            AlertDialog(
                onDismissRequest = { shouldShowErrorDialog.value = false },
                title = { Text("Payment Failed", fontWeight = FontWeight.Bold, color = Color(0xFFE53935)) },
                text = { Text("There was an issue processing your payment. Please try again.", color = Color(0xFF37474F)) },
                confirmButton = {
                    TextButton(onClick = { shouldShowErrorDialog.value = false }) {
                        Text("OK", color = Color(0xFF00897B))
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (uiState.value.isLoading) {
                Box(modifier = Modifier.fillMaxSize().padding(top = 200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF4DB6AC))
                }
            } else if (uiState.value.errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize().padding(top = 200.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Error: ${uiState.value.errorMessage}",
                            color = Color(0xFFD32F2F)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        GlassRetryButton(onClick = { viewModel.getListingDetails() })
                    }
                }
            } else {
                uiState.value.listing?.let { listing ->
                    GlassCheckoutHeader(
                        listing = listing,
                        onBackClick = { backStack.removeLastOrNull() }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Column(modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 40.dp)) {
                        listing.tripDates?.let {
                            GlassTripDates(
                                tripDates = it,
                                isSelected = uiState.value.selectedTripDateId ?: "",
                                onItemSelected = { tripDateId -> viewModel.selectTripDate(tripDateId) }
                            )
                        }
                        AnimatedVisibility((uiState.value.selectedTripDateId ?: "").isNotBlank()) {
                            GlassGuestSelector(
                                noOfGuest = uiState.value.numberOfGuests,
                                onGuestAdded = { viewModel.addGuest() },
                                onGuestRemoved = { viewModel.removeGuest() }
                            )
                        }
                        AnimatedVisibility(uiState.value.isCheckingAvailability) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(color = Color(0xFF4DB6AC), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Checking Availability...",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF5D7A74))
                                )
                            }
                        }
                        AnimatedVisibility(uiState.value.availabilityErrorMessage != null) {
                            uiState.value.availabilityErrorMessage?.let { message ->
                                GlassInfoBanner(
                                    message = message,
                                    isWarning = uiState.value.hasDateConflict
                                )
                            }
                        }
                        AnimatedVisibility(uiState.value.bookingAvailability != null) {
                            uiState.value.bookingAvailability?.let {
                                GlassBookingDetails(bookingAvailability = it)
                            }
                        }
                        if (uiState.value.bookingAvailability != null && !uiState.value.hasDateConflict) {
                            Spacer(modifier = Modifier.height(24.dp))
                            GlassPaymentButton(
                                onClick = { viewModel.createBooking() },
                                isLoading = uiState.value.creatingBooking
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GlassCheckoutHeader(listing: TravelListing, onBackClick: () -> Unit) {
    val imageUrl = listing.images?.firstOrNull()

    Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Listing Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0x33B2DFDB)),
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xAAE8F6F3)),
                        startY = 100f
                    )
                )
        )
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
        Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
            Text(
                text = listing.title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1B3A36)
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = listing.location,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF5D7A74)
                )
            )
        }
    }
}

@Composable
fun GlassTripDates(
    tripDates: List<TripDate>,
    isSelected: String,
    onItemSelected: (String) -> Unit
) {
    GlassContainer {
        Text(
            text = "Select Trip Dates",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3A36)
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        tripDates.forEachIndexed { index, tripDate ->
            val isDateSelected = isSelected == tripDate.id
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isDateSelected) Color(0x3300BFA5) else Color.Transparent)
                    .clickable { onItemSelected(tripDate.id) }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Canvas(modifier = Modifier.size(16.dp)) {
                    drawCircle(
                        color = if (isDateSelected) Color(0xFF00BFA5) else Color(0xFFB2DFDB),
                        radius = size.minDimension / 2
                    )
                    if (isDateSelected) {
                        drawCircle(
                            color = Color.White,
                            radius = size.minDimension / 5
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Check-in: ${tripDate.startDate.split("T").first()}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1B3A36)
                        )
                    )
                    Text(
                        text = "Check-out: ${tripDate.endDate.split("T").first()}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF5D7A74))
                    )
                }
                Text(
                    text = "${tripDate.availableSpots} spots",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00897B)
                    )
                )
            }
            if (index < tripDates.size - 1) {
                HorizontalDivider(color = Color(0x111B3A36), modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Composable
fun GlassGuestSelector(
    noOfGuest: Int,
    onGuestAdded: () -> Unit,
    onGuestRemoved: () -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))
    GlassContainer {
        Text(
            text = "Number of Guests",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3A36)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0x3300BFA5))
                    .clickable { onGuestRemoved() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Remove Guest",
                    tint = Color(0xFF00897B),
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = noOfGuest.toString(),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1B3A36)
                )
            )
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00BFA5))
                    .clickable { onGuestAdded() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Guest",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun GlassBookingDetails(bookingAvailability: BookingAvailability) {
    Spacer(modifier = Modifier.height(16.dp))
    GlassContainer {
        Text(
            text = "Booking Summary",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3A36)
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        bookingAvailability.priceCalculation?.let {
            GlassPriceRow(label = "Sub Total", value = "$${it.subtotal}")
            GlassPriceRow(label = "Taxes", value = "$${it.taxes}")
            GlassPriceRow(label = "Service Fee", value = "$${it.serviceFee}")
            HorizontalDivider(color = Color(0x221B3A36), modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1B3A36)
                    )
                )
                Text(
                    text = "$${it.total}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF00897B)
                    )
                )
            }
        }
    }
}

@Composable
fun GlassPriceRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF5D7A74))
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1B3A36)
            )
        )
    }
}

@Composable
fun GlassInfoBanner(message: String, isWarning: Boolean) {
    Spacer(modifier = Modifier.height(16.dp))
    val borderColor = if (isWarning) Color(0xFFFFB800) else Color(0xFFE53935)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xCCFFFFFF))
            .border(
                BorderStroke(1.dp, borderColor.copy(alpha = 0.4f)),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = if (isWarning) Color(0xFFE65100) else Color(0xFFD32F2F)
            )
        )
    }
}

@Composable
fun GlassPaymentButton(onClick: () -> Unit, isLoading: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF00BFA5), Color(0xFF00897B))
                )
            )
            .clickable(enabled = !isLoading) { onClick() }
            .padding(vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
        } else {
            Text(
                text = "Proceed to Payment",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            )
        }
    }
}

@Composable
fun GlassRetryButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xBBFFFFFF))
            .border(
                BorderStroke(1.dp, Brush.linearGradient(listOf(Color(0x44B2DFDB), Color(0x22B2DFDB)))),
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 32.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Retry",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00897B)
            )
        )
    }
}

@Composable
fun GlassContainer(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xCCFFFFFF))
            .border(
                BorderStroke(
                    1.dp, Brush.linearGradient(
                        colors = listOf(Color(0x66B2DFDB), Color(0x33B2DFDB))
                    )
                ),
                RoundedCornerShape(24.dp)
            )
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}