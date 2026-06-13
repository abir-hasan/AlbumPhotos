package com.example.albumphotos.data.album.network

import com.example.albumphotos.data.album.model.details.AlbumPhotosResponse
import com.example.albumphotos.data.album.model.list.AlbumsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumService {
    @GET("/albums")
    suspend fun getAlbums(): AlbumsResponse

    @GET("/albums/{albumId}/photos")
    suspend fun getPhotos(
        @Path("albumId") albumId: String,
    ): AlbumPhotosResponse
}
