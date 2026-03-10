package com.kaaneneskpc.travellerr.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import coil3.compose.AsyncImage
import com.kaaneneskpc.presentation.feature.details.TravelListingDetailsViewModel
import com.kaaneneskpc.travellerr.widgets.TravellerCircleImageButton
import com.kaaneneskpc.travellerr.widgets.TravellerrCircleVectorButton
import com.kaaneneskpc.travellerr.widgets.TravellerrSpacer
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import travellerr.composeapp.generated.resources.Res
import travellerr.composeapp.generated.resources.bookmark
import travellerr.composeapp.generated.resources.location
import travellerr.composeapp.generated.resources.user


@Composable
fun TravelDetailScreen(
    backStack: NavBackStack<NavKey>,
    itemId: String,
    viewModel: TravelListingDetailsViewModel = koinViewModel {
        parametersOf(itemId)
    }
) {
    val uiState = viewModel.state.collectAsState()
    Scaffold {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            if (uiState.value.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.value.errorMessage != null) {
                Text(text = uiState.value.errorMessage!!)
            } else {
                val item = uiState.value.listing!!
                Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
                    item.images?.takeIf { it.isNotEmpty() }?.let {
                        AsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = it.first(),
                            contentDescription = "Travel Image",
                            contentScale = ContentScale.Crop
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TravellerrCircleVectorButton(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Arrow",
                            modifier = Modifier,
                            onClick = {}
                        )
                        Text(
                            "Details",
                            modifier = Modifier.weight(1f).padding(16.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium
                        )
                        TravellerCircleImageButton(
                            painter = painterResource(Res.drawable.bookmark),
                            contentDescription = "bookmark",
                            modifier = Modifier,
                            onClick = {}
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize().offset(y = (-36).dp).clip(
                        RoundedCornerShape(32.dp)
                    ).background(MaterialTheme.colorScheme.background)
                )
                {

                    Row(modifier = Modifier.padding(top = 32.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title ?: "",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            Text(
                                text = item.location ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier.padding(horizontal = 16.dp),
                            )

                        }
                        TravellerCircleImageButton(
                            painter = painterResource(Res.drawable.user),
                            contentDescription = "User Image",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            onClick = {
                            }
                        )
                    }
                    TravellerrSpacer(16.dp)

                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Row(
                            Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.location),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp).padding(end = 4.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = item.country ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier,
                            )
                        }

                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "⭐ ${item.rating}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier,
                            )
                            Text(
                                "(${item.reviewCount})",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {

                            Text(
                                text = "${item.currency} ${item.price}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Blue,
                                modifier = Modifier,
                            )
                            Text(
                                text = "/person",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier,
                            )
                        }
                    }
                    item.images?.let {
                        LazyRow(modifier = Modifier.padding(16.dp)) {
                            items(it) {
                                AsyncImage(
                                    model = it, contentDescription = null,
                                    modifier = Modifier.padding(horizontal = 4.dp).size(52.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                    Text(
                        "Description",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = item.description ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
            Button(
                onClick = {
                    //backStack.add(NavRoutes.Checkout(itemId))
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Book Now", modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}