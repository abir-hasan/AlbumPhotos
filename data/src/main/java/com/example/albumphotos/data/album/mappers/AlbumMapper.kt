package com.example.albumphotos.data.album.mappers

import com.example.albumphotos.data.album.model.list.AlbumsResponse
import com.example.albumphotos.domain.album.model.Album
import org.koin.core.annotation.Factory

@Factory
class AlbumMapper {

    fun toAlbums(response: AlbumsResponse): List<Album> =
        response.map {
            Album(
                id = it.id.toString(),
                title = it.title,
            )
        }
}
