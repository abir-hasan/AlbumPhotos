package com.example.albumphotos.data.album

import com.example.albumphotos.data.album.mappers.AlbumMapper
import com.example.albumphotos.data.album.mappers.AlbumPhotoMapper
import com.example.albumphotos.data.album.network.AlbumService
import com.example.albumphotos.domain.album.AlbumRepository
import com.example.albumphotos.domain.album.model.Album
import com.example.albumphotos.domain.album.model.AlbumPhoto
import org.koin.core.annotation.Factory

@Factory
class CachedAlbumRepository(
    private val albumService: AlbumService,
    private val albumDataStore: AlbumDataStore,
    private val albumMapper: AlbumMapper,
    private val albumPhotoMapper: AlbumPhotoMapper,
) : AlbumRepository {
    override suspend fun getAlbums(isManualRefresh: Boolean): List<Album> =
        albumDataStore
            .getAlbums()
            ?.takeValue(isManualRefresh)
            ?: fetchAlbumsAndStore()

    private suspend fun fetchAlbumsAndStore(): List<Album> =
        albumService
            .getAlbums()
            .let(albumMapper::toAlbums)
            .also { albumDataStore.setAlbums(it) }

    override suspend fun getAlbumPhotos(id: String): List<AlbumPhoto> =
        albumDataStore
            .getAlbumPhotos(id)
            ?.takeValue()
            ?: fetchAlbumPhotosAndStore(id)

    private suspend fun fetchAlbumPhotosAndStore(id: String): List<AlbumPhoto> =
        albumService
            .getPhotos(id)
            .let(albumPhotoMapper::toAlbumPhotos)
            .also { albumDataStore.setAlbumPhotos(id, it) }
}
