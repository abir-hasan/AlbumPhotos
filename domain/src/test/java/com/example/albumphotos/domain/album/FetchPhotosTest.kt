package com.example.albumphotos.domain.album

import com.example.albumphotos.core.test.CoroutinesExtension
import com.example.albumphotos.domain.album.model.AlbumPhoto
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertFailsWith

@ExtendWith(MockKExtension::class, CoroutinesExtension::class)
class FetchPhotosTest {
    @MockK
    private lateinit var albumRepository: AlbumRepository

    @InjectMockKs
    private lateinit var fetchPhotos: FetchPhotos

    @Test
    fun `Given an album id, When fetch photos is invoked, then return data from repository`() =
        runTest {
            // Given
            val albumId = EasyRandom().nextObject(String::class.java)
            val photos = mockk<List<AlbumPhoto>>()
            coEvery { albumRepository.getAlbumPhotos(albumId) } returns photos

            // When
            val result = fetchPhotos(albumId)

            // Then
            kotlin.test.assertEquals(result, photos)
        }

    @Test
    fun `Given an album id, when fetch photos fails, then propagate error`() =
        runTest {
            // Given
            val albumId = EasyRandom().nextObject(String::class.java)
            val exception = RuntimeException("Something went wrong!")
            coEvery { albumRepository.getAlbumPhotos(albumId) } throws exception

            // When + Then
            assertFailsWith<RuntimeException> {
                fetchPhotos(albumId)
            }
        }
}
