package com.example.albumphotos.data.album.mappers

import com.example.albumphotos.data.album.model.details.AlbumPhotosResponse
import com.example.albumphotos.data.album.model.details.AlbumPhotosResponseItem
import com.example.albumphotos.domain.album.model.AlbumPhoto
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class AlbumPhotoMapperTest {

    @InjectMockKs
    private lateinit var albumPhotoMapper: AlbumPhotoMapper

    @Test
    fun `Given album response, when toAlbums called then return mapped data`() {
        // Given
        val response =
            AlbumPhotosResponse().apply {
                add(
                    AlbumPhotosResponseItem(
                        id = 1724,
                        albumId = 1,
                        title = "Test 1",
                        url = "http://www.test.com/full/111",
                        thumbnailUrl = "http://www.test.com/thumb/111",
                    ),
                )
                add(
                    AlbumPhotosResponseItem(
                        id = 3000,
                        albumId = 2,
                        title = "Test 2",
                        url = "http://www.test.com/full/222",
                        thumbnailUrl = "http://www.test.com/thumb/222",
                    ),
                )
            }

        // When
        val result = albumPhotoMapper.toAlbumPhotos(response)

        // Then
        assertEquals(
            expected = result,
            actual =
                listOf(
                    AlbumPhoto(
                        coverId = "1724",
                        albumId = "1",
                        url = "http://www.test.com/full/111",
                        thumbnailUrl = "http://www.test.com/thumb/111",
                        title = "Test 1",
                    ),
                    AlbumPhoto(
                        coverId = "3000",
                        albumId = "2",
                        url = "http://www.test.com/full/222",
                        thumbnailUrl = "http://www.test.com/thumb/222",
                        title = "Test 2",
                    ),
                ),
        )
    }
}
