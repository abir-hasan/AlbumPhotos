package com.example.albumphotos.presentation.album.list.model

import androidx.compose.runtime.Immutable

@Immutable
data class AlbumUIModel(
    val title: String,
    val id: String,
    val firstLetter: String,
)
