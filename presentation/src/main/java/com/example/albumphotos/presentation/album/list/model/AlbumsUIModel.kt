package com.example.albumphotos.presentation.album.list.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class AlbumsUIModel(
    val albums: ImmutableList<AlbumUIModel>,
)
