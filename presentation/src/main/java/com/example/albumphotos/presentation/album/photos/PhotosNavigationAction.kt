package com.example.albumphotos.presentation.album.photos

sealed interface PhotosNavigationAction {

    data class ShowPhoto(val url: String) : PhotosNavigationAction
}
