package com.example.albumphotos.domain.album

import com.example.albumphotos.domain.album.model.Album
import com.example.albumphotos.core.test.CoroutinesExtension
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExtendWith(MockKExtension::class, CoroutinesExtension::class)
class FetchAlbumsTest {

    @MockK
    private lateinit var albumRepository: AlbumRepository

    @InjectMockKs
    private lateinit var fetchAlbums: FetchAlbums

    @Test
    fun `When fetch albums is invoked, then return data from repository`() = runTest {
        // Given
        val forceRefresh = false
        val albums = mockk<List<Album>>()
        coEvery { albumRepository.getAlbums(forceRefresh) } returns albums

        // When
        val result = fetchAlbums(forceRefresh)

        // Then
        assertEquals(result, albums)
    }

    @Test
    fun `When fetch albums fails, then propagate error`() = runTest {
        // Given
        val forceRefresh = false
        val exception = RuntimeException("Something went wrong!")
        coEvery { albumRepository.getAlbums(forceRefresh) } throws exception

        // When + Then
        assertFailsWith<RuntimeException> {
            fetchAlbums(forceRefresh)
        }
    }
}
