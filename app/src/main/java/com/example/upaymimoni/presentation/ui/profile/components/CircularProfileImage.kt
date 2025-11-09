package com.example.upaymimoni.presentation.ui.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun CircularProfileImage(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    val safeUrl = imageUrl?.takeIf { it.isNotBlank() }
        ?: "https://api.dicebear.com/9.x/bottts-neutral/svg?seed=fallback"
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(safeUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .border(
                    BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
                    CircleShape
                )
                .clip(CircleShape)
        )
    }
}