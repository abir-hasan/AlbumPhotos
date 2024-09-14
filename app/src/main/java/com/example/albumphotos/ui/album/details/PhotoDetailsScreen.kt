package com.example.albumphotos.ui.album.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.albumphotos.ui.generic.composables.FullScreenError

@Composable
internal fun PhotoDetailsScreen(
    url: String,
    modifier: Modifier = Modifier,
) {
    ContentView(
        modifier = modifier,
        url = url,
    )
}

@Composable
private fun ContentView(
    url: String,
    modifier: Modifier = Modifier,
) = Box(
    modifier = modifier.background(color = MaterialTheme.colorScheme.background)
) {
    SubcomposeAsyncImage(
        model = url,
        modifier = Modifier.fillMaxSize(),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        loading = {
            Box {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center)
                )
            }
        },
        error = {
            FullScreenError(onClickRetry = null)
        }
    )
}
