package com.example.albumphotos.ui.album.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.NavHostController
import com.example.albumphotos.R
import com.example.albumphotos.navigation.Photos
import com.example.albumphotos.presentation.album.list.AlbumsNavigationAction
import com.example.albumphotos.presentation.album.list.AlbumsViewModel
import com.example.albumphotos.presentation.album.list.model.AlbumUIModel
import com.example.albumphotos.presentation.album.list.model.AlbumsUIModel
import com.example.albumphotos.presentation.generic.UIState
import com.example.albumphotos.presentation.generic.event.EventFlow
import com.example.albumphotos.ui.album.list.previewprovider.AlbumsPreviewParameterProvider
import com.example.albumphotos.ui.generic.composables.FullScreenError
import com.example.albumphotos.ui.generic.extension.RetrieveAsEffect
import com.example.albumphotos.ui.theme.AlbumPhotosTheme
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun AlbumsScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AlbumsViewModel = koinViewModel(),
) {
    val albumsUIState by viewModel.albumsUIState.collectAsState()
    val refreshing by viewModel.swipeRefresh.collectAsState()
    viewModel.navigation.HandleNavigationEvents(navHostController)
    Albums(
        albumsUIState = albumsUIState,
        modifier = modifier,
        onClickAlbum = viewModel::onAlbumClicked,
        onClickRetry = viewModel::onRetryClicked,
        onRefresh = viewModel::onRefreshClicked,
        refreshing = refreshing,
    )
}

@Composable
private fun Albums(
    albumsUIState: UIState<AlbumsUIModel>,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    onClickAlbum: (AlbumUIModel) -> Unit,
    onClickRetry: () -> Unit,
    modifier: Modifier = Modifier,
) = Scaffold(
    modifier = modifier.fillMaxSize(),
) {
    Crossfade(
        targetState = albumsUIState,
        label = stringResource(R.string.label_albums_sate),
        modifier = Modifier.padding(it),
    ) { state ->
        when (state) {
            is UIState.Normal -> NormalContent(
                albumsUIModel = state.data,
                onClickAlbum = onClickAlbum,
                onRefresh = onRefresh,
                refreshing = refreshing,
            )
            UIState.Loading -> LoadingContent()
            UIState.Error -> FullScreenError(
                onClickRetry = onClickRetry,
            )
        }
    }
}

@Composable
fun EventFlow<AlbumsNavigationAction>.HandleNavigationEvents(
    navHostController: NavHostController,
) {
    RetrieveAsEffect {
        when (it) {
            is AlbumsNavigationAction.OpenPhotos -> {
                navHostController.navigate(Photos(it.albumId))
            }
        }
    }
}

@Preview
@Composable
private fun NormalContentPreview(
    @PreviewParameter(AlbumsPreviewParameterProvider::class) uiState: UIState<AlbumsUIModel>,
) = AlbumPhotosTheme {
    Albums(
        albumsUIState = uiState,
        onClickAlbum = {},
        onClickRetry = {},
        onRefresh = {},
        refreshing = false,
    )
}
