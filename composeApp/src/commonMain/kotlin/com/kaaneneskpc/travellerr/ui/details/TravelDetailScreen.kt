package com.kaaneneskpc.travellerr.ui.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.kaaneneskpc.domain.model.TravelListing
import com.kaaneneskpc.presentation.feature.details.TravelListingDetailsViewModel
import com.kaaneneskpc.travellerr.navigation.NavRoutes
import com.kaaneneskpc.travellerr.ui.bookings.AnimatedAbstractBackground
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import travellerr.composeapp.generated.resources.Res
import travellerr.composeapp.generated.resources.location

@Composable
fun TravelDetailScreen(
    backStack: NavBackStack<NavKey>,
    itemId: String,
    viewModel: TravelListingDetailsViewModel = koinViewModel {
        parametersOf(itemId)
    }
) {
    val uiState = viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedAbstractBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (uiState.value.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(top = 200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF4DB6AC))
                }
            } else if (uiState.value.errorMessage != null) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(top = 200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${uiState.value.errorMessage}",
                        color = Color(0xFFD32F2F)
                    )
                }
            } else {
                uiState.value.listing?.let { listing ->
                    GlassDetailHeader(
                        listing = listing,
                        onBackClick = { backStack.removeLastOrNull() },
                        onBookmarkClick = { /* Handle Bookmark Placeholder */ }
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Column(modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 40.dp)) {
                        GlassListingInfo(listing = listing)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (!listing.description.isNullOrEmpty()) {
                            GlassDescription(description = listing.description!!)
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        if (!listing.images.isNullOrEmpty()) {
                            GlassGallery(images = listing.images!!)
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                        
                        GlassBookNowButton(onClick = { backStack.add(NavRoutes.Checkout(itemId)) })
                    }
                }
            }
        }
    }
}

@Composable
fun GlassDetailHeader(
    listing: TravelListing,
    onBackClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    val imageUrl = listing.images?.firstOrNull()

    Box(modifier = Modifier.fillMaxWidth().height(360.dp)) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Travel Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0x33B2DFDB)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF4DB6AC))
            }
        }
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xAAE8F6F3)),
                        startY = 400f // Push gradient lower
                    )
                )
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
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
            
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xBBFFFFFF))
                    .border(
                        BorderStroke(1.dp, Brush.linearGradient(listOf(Color(0x44B2DFDB), Color(0x22B2DFDB)))),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onBookmarkClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = Color(0xFF1B3A36),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun GlassListingInfo(listing: TravelListing) {
    DetailGlassContainer {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = listing.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1B3A36)
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.location),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${listing.location}, ${listing.country}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF5D7A74)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color(0x111B3A36))
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFF6B35),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${listing.rating}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B3A36)
                    )
                )
                Text(
                    text = " (${listing.reviewCount} reviews)",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF5D7A74)
                    )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "${listing.currency} ${listing.price}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF00897B)
                    )
                )
                Text(
                    text = "/person",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF5D7A74)
                    ),
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

@Composable
fun GlassDescription(description: String) {
    DetailGlassContainer {
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3A36)
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF37474F),
                lineHeight = 22.sp
            )
        )
    }
}

@Composable
fun GlassGallery(images: List<String>) {
    DetailGlassContainer {
        Text(
            text = "Gallery",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B3A36)
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(images) { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Gallery Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x33B2DFDB)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun GlassBookNowButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF00BFA5), Color(0xFF00897B))
                )
            )
            .clickable { onClick() }
            .padding(vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Book Now",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.sp
            )
        )
    }
}

@Composable
fun DetailGlassContainer(content: @Composable () -> Unit) {
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