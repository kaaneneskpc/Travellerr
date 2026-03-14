package com.kaaneneskpc.travellerr.ui.listing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kaaneneskpc.domain.model.TravelListing
import com.kaaneneskpc.presentation.feature.listings.TravelListingViewModel
import com.kaaneneskpc.travellerr.navigation.NavRoutes
import com.kaaneneskpc.travellerr.theme.Orange
import com.kaaneneskpc.travellerr.widgets.MultiHighlightedText
import com.kaaneneskpc.travellerr.widgets.TextHighlight
import com.kaaneneskpc.travellerr.widgets.TravellerrSpacer
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import travellerr.composeapp.generated.resources.Res
import travellerr.composeapp.generated.resources.dummy
import travellerr.composeapp.generated.resources.bookmark
import travellerr.composeapp.generated.resources.notifications
import travellerr.composeapp.generated.resources.user
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey


@Composable
fun HomeListingScreen(backStack: NavBackStack<NavKey>, viewModel: TravelListingViewModel = koinViewModel()) {

    val uiState = viewModel.state.collectAsState()
    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clip(RoundedCornerShape(32.dp)).background(
                        color = Color.LightGray.copy(alpha = 0.2f)
                    ).padding(4.dp)
                ) {
                    Image(painter = painterResource(Res.drawable.user), contentDescription = null)
                    Text("Kaan", modifier = Modifier.padding(horizontal = 8.dp))
                }
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(Res.drawable.bookmark),
                    contentDescription = "Bookings",
                    modifier = Modifier.size(48.dp)
                        .clip(CircleShape)
                        .background(color = Color.LightGray.copy(alpha = 0.2f))
                        .clickable { backStack.add(NavRoutes.BookingList) }
                        .padding(12.dp),
                )
                Spacer(Modifier.width(8.dp))
                Image(
                    painter = painterResource(Res.drawable.notifications),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp).clip(CircleShape).background(
                        color = Color.LightGray.copy(alpha = 0.2f)
                    ).padding(12.dp),
                )
            }
            MultiHighlightedText(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                fullText = "Explore the \nbeautiful world!",
                highlights = listOf(

                    TextHighlight(
                        word = "world!",
                        color = Orange,
                        showUnderline = true,
                        fontWeight = FontWeight.Bold
                    ),
                    TextHighlight(
                        word = "beautiful",
                        color = Color.Black,
                        showUnderline = false,
                        fontWeight = FontWeight.Bold
                    )
                ),
                fontSize = 44.sp,
                peakHeight = 14.dp,
                thickness = 10.dp
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Best Destination", fontWeight = FontWeight.Medium, fontSize = 18.sp)
                Spacer(Modifier.weight(1f))
                TextButton(onClick = { /*TODO*/ }) {
                    Text("See All", color = Orange)
                }
            }
            TravellerrSpacer(8.dp)
            if (uiState.value.isLoading) {
                // Placeholder for loading state
                CircularProgressIndicator()
            }
            uiState.value.errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            uiState.value.listings.takeIf { it.isNotEmpty() }?.let {
                LazyRow {
                    items(it.size) { index ->
                        DestinationCard(model = it[index]) {
                            backStack.add(NavRoutes.ListingDetails(id = it.id))
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DestinationCard(model: TravelListing, onItemClick: (TravelListing) -> Unit) {
    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .width(250.dp)
            .background(Color.LightGray.copy(alpha = 0.1f), shape = RoundedCornerShape(16.dp))
            .clickable {
                onItemClick(model)
            }
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = model.images?.firstOrNull() ?: painterResource(Res.drawable.dummy),
            contentDescription = null,
            modifier = Modifier
                .size(width = 230.dp, height = 300.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        TravellerrSpacer(8.dp)
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text(
                text = model.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = "${model.rating}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        TravellerrSpacer(5.dp)
        Text(
            text = model.location,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            color = Color.Gray,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
@Preview(showBackground = true)
fun HomeListingScreenPreview() {
    //HomeListingScreen()
}