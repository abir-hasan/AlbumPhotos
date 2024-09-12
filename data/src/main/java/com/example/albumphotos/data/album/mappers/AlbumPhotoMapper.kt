package com.example.albumphotos.data.album.mappers

import com.example.albumphotos.data.album.model.details.AlbumPhotosResponse
import com.example.albumphotos.domain.album.model.AlbumPhoto
import org.koin.core.annotation.Factory

@Factory
class AlbumPhotoMapper {

    fun toAlbumPhotos(response: AlbumPhotosResponse): List<AlbumPhoto> {
        return response.map {
            AlbumPhoto(
                albumId = it.albumId.toString(),
                coverId = it.id.toString(),
                thumbnailUrl = it.thumbnailUrl,
                url = it.url,
            )
        }
    }
}
