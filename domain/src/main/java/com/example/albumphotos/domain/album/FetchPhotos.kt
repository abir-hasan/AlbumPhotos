package com.example.albumphotos.domain.album

import com.example.albumphotos.domain.album.model.AlbumPhoto
import org.koin.core.annotation.Factory

@Factory
class FetchPhotos(
    private val albumRepository: AlbumRepository,
) {

    suspend operator fun invoke(id: String): List<AlbumPhoto> = albumRepository.getAlbumPhotos(id)
}
