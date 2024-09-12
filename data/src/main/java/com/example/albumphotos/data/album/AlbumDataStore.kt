package com.example.albumphotos.data.album

import com.example.albumphotos.data.generic.CachedValue
import com.example.albumphotos.domain.album.model.Album
import com.example.albumphotos.domain.album.model.AlbumPhoto

interface AlbumDataStore {

    fun setAlbums(albums: List<Album>)
    fun getAlbums(): CachedValue<List<Album>>?
    fun setAlbumPhotos(albumId: String, albumPhotos: List<AlbumPhoto>)
    fun getAlbumPhotos(albumId: String): CachedValue<List<AlbumPhoto>>?
}
