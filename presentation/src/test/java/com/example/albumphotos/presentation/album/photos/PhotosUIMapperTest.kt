package com.example.albumphotos.presentation.album.photos

import com.example.albumphotos.domain.album.model.AlbumPhoto
import com.example.albumphotos.presentation.album.photos.model.PhotoUIModel
import com.example.albumphotos.presentation.album.photos.model.PhotosUIModel
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.collections.immutable.toImmutableList
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class PhotosUIMapperTest {

    @InjectMockKs
    private lateinit var photosUIMapper: PhotosUIMapper

    @Test
    fun `Given list of photos, when toUIModel called, then return mapped ui models`() {
        // Given
        val photos = listOf(
            AlbumPhoto(
                albumId = "XYZ",
                title = "new 1",
                coverId = "1",
                url = "http://www.test.com/full/1",
                thumbnailUrl = "http://www.test.com/thumb/1",
            ),
            AlbumPhoto(
                albumId = "XYZ",
                title = "new 2",
                coverId = "2",
                url = "http://www.test.com/full/2",
                thumbnailUrl = "http://www.test.com/thumb/2",
            ),
        )

        // When
        val result = photosUIMapper.toUIModel(photos)

        // Then
        kotlin.test.assertEquals(
            expected = result,
            actual = PhotosUIModel(
                listOf(
                    PhotoUIModel(
                        coverId = "1",
                        title = "new 1",
                        url = "http://www.test.com/full/1",
                        thumbnailUrl = "http://www.test.com/thumb/1",
                    ),
                    PhotoUIModel(
                        coverId = "2",
                        title = "new 2",
                        url = "http://www.test.com/full/2",
                        thumbnailUrl = "http://www.test.com/thumb/2",
                    ),
                ).toImmutableList()
            )
        )
    }
}
