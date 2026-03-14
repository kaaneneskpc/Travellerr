package com.kaaneneskpc.travellerr.ui.listing

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import coil3.compose.AsyncImage
import com.kaaneneskpc.domain.model.TravelListing
import com.kaaneneskpc.presentation.feature.listings.TravelListingViewModel
import com.kaaneneskpc.travellerr.navigation.NavRoutes
import org.koin.compose.viewmodel.koinViewModel

private val TealPrimary = Color(0xFF00897B)
private val TealLight = Color(0xFF4DB6AC)
private val TealDark = Color(0xFF1B3A36)
private val SoftGray = Color(0xFF5D7A74)
private val WarmOrange = Color(0xFFFF6B35)
private val SoftCream = Color(0xFFF5F0E8)

val CardSlantedShape = GenericShape { size, _ ->
    val slant = size.width * 0.08f
    moveTo(slant, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width - slant, size.height)
    lineTo(0f, size.height)
    close()
}

@Composable
fun HomeAnimatedBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "homeBackground")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    val wave by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave"
    )
    Canvas(modifier = Modifier.fillMaxSize().background(SoftCream)) {
        val width = size.width
        val height = size.height
        translate(
            left = (rotation / 360f) * width * 0.05f,
            top = (wave) * height * 0.02f
        ) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x6696E6D0), Color.Transparent),
                    center = Offset(width * 0.15f, height * 0.1f),
                    radius = width * 0.6f * pulse
                ),
                size = size
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x55FFE0B2), Color.Transparent),
                    center = Offset(width * 0.85f, height * 0.3f),
                    radius = width * 0.5f / pulse
                ),
                size = size
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x44B2DFDB), Color.Transparent),
                    center = Offset(width * 0.5f, height * 0.7f),
                    radius = width * 0.7f * pulse
                ),
                size = size
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x33FFCCBC), Color.Transparent),
                    center = Offset(width * 0.2f, height * 0.9f),
                    radius = width * 0.4f
                ),
                size = size
            )
        }
    }
}

@Composable
fun HomeListingScreen(
    backStack: NavBackStack<NavKey>,
    viewModel: TravelListingViewModel = koinViewModel()
) {
    val uiState = viewModel.state.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        HomeAnimatedBackground()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(48.dp))
                GlassHeader(
                    userName = "Kaan",
                    onBookingsClick = { backStack.add(NavRoutes.BookingList) },
                    onNotificationsClick = { }
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                WelcomeSection()
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                GlassSearchBar()
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                CategorySection()
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader(
                    title = "Featured Destinations",
                    onSeeAllClick = { }
                )
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
                if (uiState.value.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(320.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        GlassLoadingIndicator()
                    }
                } else if (uiState.value.errorMessage != null) {
                    GlassErrorCard(message = uiState.value.errorMessage!!)
                } else {
                    FeaturedDestinationsRow(
                        listings = uiState.value.listings,
                        onItemClick = { listing ->
                            backStack.add(NavRoutes.ListingDetails(id = listing.id))
                        }
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader(
                    title = "Popular Now",
                    onSeeAllClick = { }
                )
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
                if (!uiState.value.isLoading && uiState.value.listings.isNotEmpty()) {
                    PopularDestinationsRow(
                        listings = uiState.value.listings.shuffled().take(5),
                        onItemClick = { listing ->
                            backStack.add(NavRoutes.ListingDetails(id = listing.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GlassHeader(
    userName: String,
    onBookingsClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xBBFFFFFF))
                .border(
                    BorderStroke(1.dp, Brush.linearGradient(
                        colors = listOf(Color(0x44B2DFDB), Color(0x22B2DFDB))
                    )),
                    RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(TealLight, TealPrimary)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.first().toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Welcome back",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SoftGray
                        )
                    )
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TealDark
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        GlassIconButton(
            icon = Icons.Outlined.Bookmark,
            onClick = onBookingsClick,
            badgeCount = null
        )
        Spacer(modifier = Modifier.width(12.dp))
        GlassIconButton(
            icon = Icons.Outlined.Notifications,
            onClick = onNotificationsClick,
            badgeCount = 3
        )
    }
}

@Composable
fun GlassIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    badgeCount: Int? = null
) {
    Box {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xBBFFFFFF))
                .border(
                    BorderStroke(1.dp, Brush.linearGradient(
                        colors = listOf(Color(0x44B2DFDB), Color(0x22B2DFDB))
                    )),
                    CircleShape
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TealDark,
                modifier = Modifier.size(24.dp)
            )
        }
        if (badgeCount != null && badgeCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 2.dp, y = (-2).dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(WarmOrange),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (badgeCount > 9) "9+" else badgeCount.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun WelcomeSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Explore the",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Normal,
                color = TealDark,
                fontSize = 36.sp
            )
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Beautiful ",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TealDark,
                    fontSize = 36.sp
                )
            )
            Box {
                Text(
                    text = "World!",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = WarmOrange,
                        fontSize = 36.sp
                    )
                )
                Canvas(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(top = 38.dp)
                ) {
                    val wavePoints = 20
                    val amplitude = 4.dp.toPx()
                    val pathWidth = size.width
                    for (i in 0 until wavePoints) {
                        val x = (i.toFloat() / wavePoints) * pathWidth
                        val y = amplitude * kotlin.math.sin((i.toFloat() / wavePoints) * 2 * kotlin.math.PI).toFloat()
                        drawCircle(
                            color = WarmOrange,
                            radius = 3.dp.toPx(),
                            center = Offset(x, y + amplitude)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GlassSearchBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xCCFFFFFF))
            .border(
                BorderStroke(1.dp, Brush.linearGradient(
                    colors = listOf(Color(0x66B2DFDB), Color(0x33B2DFDB))
                )),
                RoundedCornerShape(20.dp)
            )
            .clickable { }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = TealPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Search destinations, hotels...",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = SoftGray
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(TealLight, TealPrimary)
                        )
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Filter",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

data class CategoryItem(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun CategorySection() {
    val categories = listOf(
        CategoryItem("Beach", Icons.Outlined.BeachAccess, Color(0xFF4FC3F7)),
        CategoryItem("Mountain", Icons.Outlined.Terrain, Color(0xFF81C784)),
        CategoryItem("City", Icons.Outlined.LocationCity, Color(0xFFFFB74D)),
        CategoryItem("Forest", Icons.Outlined.Park, Color(0xFF66BB6A)),
        CategoryItem("Desert", Icons.Outlined.WbSunny, Color(0xFFFFCA28))
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        categories.forEach { category ->
            GlassCategoryChip(category = category)
        }
    }
}

@Composable
fun GlassCategoryChip(category: CategoryItem) {
    var isSelected by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) category.color.copy(alpha = 0.2f) else Color(0xBBFFFFFF)
            )
            .border(
                BorderStroke(
                    1.dp,
                    if (isSelected) Brush.linearGradient(listOf(category.color.copy(alpha = 0.5f), category.color.copy(alpha = 0.3f)))
                    else Brush.linearGradient(listOf(Color(0x44B2DFDB), Color(0x22B2DFDB)))
                ),
                RoundedCornerShape(16.dp)
            )
            .clickable { isSelected = !isSelected }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                tint = if (isSelected) category.color else SoftGray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) category.color else TealDark
                )
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    onSeeAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TealDark
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = onSeeAllClick) {
            Text(
                text = "See All",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = WarmOrange
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = WarmOrange,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun FeaturedDestinationsRow(
    listings: List<TravelListing>,
    onItemClick: (TravelListing) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(listings, key = { it.id }) { listing ->
            FeaturedDestinationCard(
                listing = listing,
                onClick = { onItemClick(listing) }
            )
        }
    }
}

@Composable
fun FeaturedDestinationCard(
    listing: TravelListing,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cardShimmer")
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(360.dp)
            .clip(RoundedCornerShape(24.dp))
            .shadow(8.dp, RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = listing.images?.firstOrNull(),
            contentDescription = listing.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xBBFFFFFF))
                .border(
                    BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                    RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = WarmOrange,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${listing.rating}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TealDark
                    )
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .clip(CircleShape)
                .background(Color(0x66FFFFFF))
                .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)), CircleShape)
                .clickable { }
                .padding(10.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Text(
                text = listing.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = TealLight,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = listing.location,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.9f)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$${listing.price}",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "/person",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(TealLight, TealPrimary)
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Book",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun PopularDestinationsRow(
    listings: List<TravelListing>,
    onItemClick: (TravelListing) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(listings, key = { "popular_${it.id}" }) { listing ->
            PopularDestinationCard(
                listing = listing,
                onClick = { onItemClick(listing) }
            )
        }
    }
}

@Composable
fun PopularDestinationCard(
    listing: TravelListing,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .height(220.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xCCFFFFFF))
            .border(
                BorderStroke(1.dp, Brush.linearGradient(
                    colors = listOf(Color(0x66B2DFDB), Color(0x33B2DFDB))
                )),
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AsyncImage(
                    model = listing.images?.firstOrNull(),
                    contentDescription = listing.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0x99FFFFFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = TealDark,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Text(
                    text = listing.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TealDark
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            tint = SoftGray,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = listing.location.split(",").firstOrNull() ?: listing.location,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SoftGray
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = 60.dp)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = WarmOrange,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${listing.rating}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TealDark
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GlassLoadingIndicator() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xBBFFFFFF))
            .border(
                BorderStroke(1.dp, Brush.linearGradient(
                    colors = listOf(Color(0x44B2DFDB), Color(0x22B2DFDB))
                )),
                RoundedCornerShape(20.dp)
            )
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = TealPrimary,
                strokeWidth = 3.dp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Loading destinations...",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = SoftGray
                )
            )
        }
    }
}

@Composable
fun GlassErrorCard(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x22E53935))
            .border(
                BorderStroke(1.dp, Color(0x44E53935)),
                RoundedCornerShape(20.dp)
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = Color(0xFFE53935),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFFB71C1C)
                )
            )
        }
    }
}
