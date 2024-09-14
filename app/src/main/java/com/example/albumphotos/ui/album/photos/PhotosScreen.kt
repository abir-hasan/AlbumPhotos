package com.example.albumphotos.ui.album.photos

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.example.albumphotos.R
import com.example.albumphotos.presentation.album.photos.PhotosViewModel
import com.example.albumphotos.presentation.album.photos.model.PhotoUIModel
import com.example.albumphotos.presentation.album.photos.model.PhotosArg
import com.example.albumphotos.presentation.album.photos.model.PhotosUIModel
import com.example.albumphotos.presentation.generic.UIState
import com.example.albumphotos.ui.generic.composables.FullScreenError
import com.example.albumphotos.ui.theme.Shapes
import com.example.albumphotos.ui.theme.Spacing
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PhotosScreen(
    albumId: String,
    modifier: Modifier = Modifier,
    viewModel: PhotosViewModel = koinViewModel(
        parameters = { parametersOf(PhotosArg(albumId)) },
    )
) {
    val albumsUIState by viewModel.photosUIState.collectAsState()
    Photos(
        photosState = albumsUIState,
        modifier = modifier,
        onClickRetry = viewModel::onRetryClicked,
    )
}

@Composable
private fun Photos(
    photosState: UIState<PhotosUIModel>,
    onClickRetry: () -> Unit,
    modifier: Modifier = Modifier,
) = Scaffold(
    modifier = modifier.fillMaxSize(),
) {
    Crossfade(
        targetState = photosState,
        label = stringResource(R.string.label_photos_sate),
        modifier = Modifier.padding(it),
    ) { state ->
        when (state) {
            is UIState.Normal -> NormalContent(
                photosUIModel = state.data,
            )
            UIState.Loading -> {
                LoadingContent(modifier = Modifier.fillMaxSize())
            }
            UIState.Error -> FullScreenError(
                onClickRetry = onClickRetry,
            )
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
) {
    CircularProgressIndicator()
}

@Composable
private fun NormalContent(
    photosUIModel: PhotosUIModel,
    modifier: Modifier = Modifier,
) = LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    modifier = modifier.padding(Spacing.x1),
    horizontalArrangement = Arrangement.spacedBy(Spacing.x1),
    verticalArrangement = Arrangement.spacedBy(Spacing.x0_5),
) {
    items(photosUIModel.photos) {
        PhotoItem(
            photoUIModel = it,
            modifier = Modifier
        )
    }
}

@Composable
private fun PhotoItem(
    photoUIModel: PhotoUIModel,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier
        .padding(Spacing.x1)
        .clip(Shapes.medium)
        .background(
            color = MaterialTheme.colorScheme.surface,
            shape = Shapes.medium,
        )
) {
    AsyncImage(
        model = photoUIModel.thumbnailUrl,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1F),
        contentDescription = null,
    )
    Text(
        text = photoUIModel.title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.tertiary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(Spacing.x1),
        maxLines = 1,
    )
}



