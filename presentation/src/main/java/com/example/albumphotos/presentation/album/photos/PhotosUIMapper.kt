package com.example.albumphotos.presentation.album.photos

import com.example.albumphotos.domain.album.model.AlbumPhoto
import com.example.albumphotos.presentation.album.photos.model.PhotoUIModel
import com.example.albumphotos.presentation.album.photos.model.PhotosUIModel
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.Factory

@Factory
class PhotosUIMapper {
    fun toUIModel(photos: List<AlbumPhoto>): PhotosUIModel =
        PhotosUIModel(
            photos = photos.toUIModel().toImmutableList(),
        )

    private fun List<AlbumPhoto>.toUIModel() =
        map {
            PhotoUIModel(
                coverId = it.coverId,
                thumbnailUrl = it.thumbnailUrl,
                url = it.url,
                title = it.title,
            )
        }
}
