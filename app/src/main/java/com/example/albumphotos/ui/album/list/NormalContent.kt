package com.example.albumphotos.ui.album.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.albumphotos.presentation.album.list.model.AlbumUIModel
import com.example.albumphotos.presentation.album.list.model.AlbumsUIModel
import com.example.albumphotos.ui.theme.Shapes
import com.example.albumphotos.ui.theme.Spacing

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun NormalContent(
    albumsUIModel: AlbumsUIModel,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    onClickAlbum: (AlbumUIModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullRefreshState = rememberPullRefreshState(refreshing, onRefresh)
    Box(modifier = modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(
            modifier = Modifier.padding(Spacing.x1),
            verticalArrangement = Arrangement.spacedBy(Spacing.x1),
        ) {
            items(albumsUIModel.albums) {
                AlbumItem(
                    albumUIModel = it,
                    modifier = Modifier,
                    onClickAlbum = onClickAlbum,
                )
            }
        }
        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@Composable
private fun AlbumItem(
    albumUIModel: AlbumUIModel,
    onClickAlbum: (AlbumUIModel) -> Unit,
    modifier: Modifier = Modifier,
) = Card(
    modifier =
        modifier
            .clip(shape = Shapes.medium)
            .clickable { onClickAlbum(albumUIModel) },
    shape = Shapes.medium,
) {
    val gradient =
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.inversePrimary,
        )

    Row(
        modifier =
            Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .padding(Spacing.x2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(90.dp)
                    .border(
                        border = BorderStroke(width = 1.dp, brush = Brush.horizontalGradient(colors = gradient)),
                        shape = CircleShape,
                    ).clip(CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = albumUIModel.firstLetter,
                style =
                    MaterialTheme.typography.displayLarge.copy(
                        brush = Brush.verticalGradient(colors = gradient),
                    ),
                textAlign = TextAlign.Center,
            )
        }
        Text(
            text = albumUIModel.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = Spacing.x2),
        )
    }
}
