package com.example.albumphotos.domain.album.model

data class AlbumPhoto(
    val coverId: String,
    val albumId: String,
    val thumbnailUrl: String,
    val url: String,
    val title: String,
)
