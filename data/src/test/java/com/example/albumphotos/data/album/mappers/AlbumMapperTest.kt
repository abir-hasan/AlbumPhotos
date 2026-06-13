package com.example.albumphotos.data.album.mappers

import com.example.albumphotos.data.album.model.list.AlbumsResponse
import com.example.albumphotos.data.album.model.list.AlbumsResponseItem
import com.example.albumphotos.domain.album.model.Album
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class AlbumMapperTest {
    @InjectMockKs
    private lateinit var albumMapper: AlbumMapper

    @Test
    fun `Given album response, when toAlbums called then return mapped data`() {
        // Given
        val response =
            AlbumsResponse().apply {
                add(
                    AlbumsResponseItem(
                        id = 1724,
                        title = "Test 1",
                        userId = 6941,
                    ),
                )
                add(
                    AlbumsResponseItem(
                        id = 2000,
                        title = "Test 2",
                        userId = 6941,
                    ),
                )
            }

        // When
        val result = albumMapper.toAlbums(response)

        // Then
        assertEquals(
            expected = result,
            actual =
                listOf(
                    Album(
                        id = "1724",
                        title = "Test 1",
                    ),
                    Album(
                        id = "2000",
                        title = "Test 2",
                    ),
                ),
        )
    }
}
