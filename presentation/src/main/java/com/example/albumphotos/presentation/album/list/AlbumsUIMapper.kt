package com.example.albumphotos.presentation.album.list

import com.example.albumphotos.domain.album.model.Album
import com.example.albumphotos.presentation.album.list.model.AlbumUIModel
import com.example.albumphotos.presentation.album.list.model.AlbumsUIModel
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.Factory
import java.util.Locale

@Factory
class AlbumsUIMapper {
    fun toUIModel(albums: List<Album>): AlbumsUIModel =
        AlbumsUIModel(
            albums = albums.toUIModel().toImmutableList(),
        )

    private fun List<Album>.toUIModel() =
        map {
            val capitalisedTitle = it.title.capitalise()
            AlbumUIModel(
                id = it.id,
                title = capitalisedTitle,
                firstLetter = capitalisedTitle[FirstCharacterIndex].toString(),
            )
        }

    private fun String.capitalise(): String =
        replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }

    companion object {
        private const val FirstCharacterIndex = 0
    }
}
