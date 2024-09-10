package com.example.albumphotos.data.album

import com.example.albumphotos.data.album.network.AlbumService
import com.example.albumphotos.domain.album.AlbumRepository
import com.example.albumphotos.domain.album.model.Album
import com.example.albumphotos.domain.album.model.AlbumPhoto
import org.koin.core.annotation.Factory

@Factory
class CachedAlbumRepository(
    private val albumService: AlbumService,
) : AlbumRepository {

    override suspend fun getAlbums(): List<Album> {
        return albumService.getAlbums().map {
            Album(id = it.id.toString(), title = it.title) // TODO
        }
    }

    override suspend fun getAlbumPhotos(id: String): List<AlbumPhoto> {
        TODO("Not yet implemented")
    }
}
