package com.example.albumphotos.presentation.album.photos.model

import androidx.compose.runtime.Immutable

@Immutable
data class PhotoUIModel(
    val coverId: String,
    val thumbnailUrl: String,
    val url: String,
    val title: String,
)
