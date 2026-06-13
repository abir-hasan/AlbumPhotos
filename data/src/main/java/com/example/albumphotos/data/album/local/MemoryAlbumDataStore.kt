package com.example.albumphotos.data.album.local

import com.example.albumphotos.data.album.AlbumDataStore
import com.example.albumphotos.data.generic.CachedValue
import com.example.albumphotos.domain.album.model.Album
import com.example.albumphotos.domain.album.model.AlbumPhoto
import org.koin.core.annotation.Single

@Single
class MemoryAlbumDataStore : AlbumDataStore {

    private var cachedAlbums: CachedValue<List<Album>>? = null

    private val cachedAlbumPhotoMap = mutableMapOf<String, CachedValue<List<AlbumPhoto>>>()

    override fun setAlbums(albums: List<Album>) {
        cachedAlbums = CachedValue(albums)
    }

    override fun getAlbums(): CachedValue<List<Album>>? = cachedAlbums

    override fun setAlbumPhotos(
        albumId: String,
        albumPhotos: List<AlbumPhoto>,
    ) {
        cachedAlbumPhotoMap[albumId] = CachedValue(value = albumPhotos)
    }

    override fun getAlbumPhotos(albumId: String): CachedValue<List<AlbumPhoto>>? = cachedAlbumPhotoMap[albumId]
}
