package com.example.albumphotos.domain.album

import com.example.albumphotos.domain.album.model.Album
import org.koin.core.annotation.Factory

@Factory
class FetchAlbums(
    private val albumRepository: AlbumRepository,
) {

    suspend operator fun invoke(): List<Album> {
        return albumRepository.getAlbums()
    }
}
