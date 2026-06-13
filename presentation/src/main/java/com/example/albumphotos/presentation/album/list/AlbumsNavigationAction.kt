package com.example.albumphotos.presentation.album.list

sealed interface AlbumsNavigationAction {
    data class OpenPhotos(
        val albumId: String,
    ) : AlbumsNavigationAction
}
