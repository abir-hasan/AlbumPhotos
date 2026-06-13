package com.example.albumphotos.ui.album.photos

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.albumphotos.R
import com.example.albumphotos.navigation.PhotoDetails
import com.example.albumphotos.presentation.album.photos.PhotosNavigationAction
import com.example.albumphotos.presentation.album.photos.PhotosViewModel
import com.example.albumphotos.presentation.album.photos.model.PhotoUIModel
import com.example.albumphotos.presentation.album.photos.model.PhotosArg
import com.example.albumphotos.presentation.album.photos.model.PhotosUIModel
import com.example.albumphotos.presentation.generic.UIState
import com.example.albumphotos.presentation.generic.event.EventFlow
import com.example.albumphotos.ui.generic.composables.FullScreenError
import com.example.albumphotos.ui.generic.extension.RetrieveAsEffect
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun PhotosScreen(
    albumId: String,
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: PhotosViewModel =
        koinViewModel(
            parameters = { parametersOf(PhotosArg(albumId)) },
        ),
) {
    val albumsUIState by viewModel.photosUIState.collectAsState()
    viewModel.navigation.HandleNavigationEvents(navHostController)
    Photos(
        photosState = albumsUIState,
        modifier = modifier,
        onClickRetry = viewModel::onRetryClicked,
        onClickPhoto = viewModel::onPhotoClicked,
    )
}

@Composable
private fun Photos(
    photosState: UIState<PhotosUIModel>,
    onClickRetry: () -> Unit,
    onClickPhoto: (PhotoUIModel) -> Unit,
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
            is UIState.Normal ->
                NormalContent(
                    photosUIModel = state.data,
                    onClickPhoto = onClickPhoto,
                )
            UIState.Loading -> {
                LoadingContent(modifier = Modifier.fillMaxSize())
            }
            UIState.Error ->
                FullScreenError(
                    onClickRetry = onClickRetry,
                )
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) =
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }

@Composable
fun EventFlow<PhotosNavigationAction>.HandleNavigationEvents(navHostController: NavHostController) {
    RetrieveAsEffect {
        when (it) {
            is PhotosNavigationAction.ShowPhoto -> {
                navHostController.navigate(PhotoDetails(it.url))
            }
        }
    }
}
