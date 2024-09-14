package com.example.albumphotos.ui.album.photos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.example.albumphotos.presentation.album.photos.model.PhotoUIModel
import com.example.albumphotos.presentation.album.photos.model.PhotosUIModel
import com.example.albumphotos.ui.theme.Shapes
import com.example.albumphotos.ui.theme.Spacing

@Composable
internal fun NormalContent(
    photosUIModel: PhotosUIModel,
    onClickPhoto: (PhotoUIModel) -> Unit,
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
            onClickPhoto = onClickPhoto,
        )
    }
}

@Composable
private fun PhotoItem(
    photoUIModel: PhotoUIModel,
    onClickPhoto: (PhotoUIModel) -> Unit,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier
        .padding(Spacing.x1)
        .clickable { onClickPhoto(photoUIModel) }
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
