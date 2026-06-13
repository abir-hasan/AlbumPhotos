package com.example.albumphotos.domain.album

import com.example.albumphotos.domain.album.model.Album
import com.example.albumphotos.domain.album.model.AlbumPhoto

interface AlbumRepository {

    suspend fun getAlbums(isManualRefresh: Boolean): List<Album>

    suspend fun getAlbumPhotos(id: String): List<AlbumPhoto>
}
