package com.example.albumphotos.presentation.album.photos.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class PhotosUIModel(
    val photos: ImmutableList<PhotoUIModel>,
)
