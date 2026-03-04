package com.kaaneneskpc.travellerr.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
fun TravellerrCircleVectorButton(
    modifier: Modifier,
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,

    ) {
    Image(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.padding(16.dp).size(48.dp).clip(CircleShape)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)).clickable {
                onClick()
            }.padding(12.dp))

}

@Composable
fun TravellerCircleImageButton(
    modifier: Modifier,
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier.padding(16.dp).size(48.dp).clip(CircleShape)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)).clickable {
                onClick()
            }.padding(8.dp))

}