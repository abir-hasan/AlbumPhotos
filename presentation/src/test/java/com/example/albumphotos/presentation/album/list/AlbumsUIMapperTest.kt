package com.example.albumphotos.presentation.album.list

import com.example.albumphotos.domain.album.model.Album
import com.example.albumphotos.presentation.album.list.model.AlbumUIModel
import com.example.albumphotos.presentation.album.list.model.AlbumsUIModel
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.collections.immutable.toImmutableList
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class AlbumsUIMapperTest {

    @InjectMockKs
    private lateinit var albumsUIMapper: AlbumsUIMapper

    @Test
    fun `Given list of album, when toUIModel called, then return mapped ui models`() {
        // Given
        val albums = listOf(
            Album(
                id = "1",
                title = "trust",
            ),
            Album(
                id = "2",
                title = "emptiness machine",
            )
        )

        // When
        val result = albumsUIMapper.toUIModel(albums)

        // Then
        assertEquals(
            expected = result,
            actual = AlbumsUIModel(
                listOf(
                    AlbumUIModel(
                        id = "1",
                        title = "Trust",
                        firstLetter = "T",
                    ),
                    AlbumUIModel(
                        id = "2",
                        title = "Emptiness machine",
                        firstLetter = "E",
                    ),
                ).toImmutableList()
            )
        )
    }
}
