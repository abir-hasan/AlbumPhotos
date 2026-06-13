package com.example.albumphotos.data.album

import com.example.albumphotos.core.test.CoroutinesExtension
import com.example.albumphotos.data.album.mappers.AlbumMapper
import com.example.albumphotos.data.album.mappers.AlbumPhotoMapper
import com.example.albumphotos.data.album.model.details.AlbumPhotosResponse
import com.example.albumphotos.data.album.model.list.AlbumsResponse
import com.example.albumphotos.data.album.network.AlbumService
import com.example.albumphotos.data.generic.CachedValue
import com.example.albumphotos.domain.album.model.Album
import com.example.albumphotos.domain.album.model.AlbumPhoto
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class, CoroutinesExtension::class)
internal class CachedAlbumRepositoryTest {
    @MockK
    private lateinit var albumService: AlbumService

    @MockK(relaxUnitFun = true)
    private lateinit var albumDataStore: AlbumDataStore

    @MockK
    private lateinit var albumMapper: AlbumMapper

    @MockK
    private lateinit var albumPhotoMapper: AlbumPhotoMapper

    @InjectMockKs
    private lateinit var repository: CachedAlbumRepository

    @Test
    fun `Given no album data, when getAlbum is called, then fetch data from remote, map it then store`() =
        runTest {
            // Given
            val isManualRefresh = false
            val albumResponse = mockk<AlbumsResponse>()
            val albumList = mockk<List<Album>>()
            every { albumDataStore.getAlbums() } returns null
            coEvery { albumService.getAlbums() } returns albumResponse
            every { albumMapper.toAlbums(albumResponse) } returns albumList

            // When
            val result = repository.getAlbums(isManualRefresh)

            // Then
            assertEquals(
                actual = result,
                expected = albumList,
            )
            verify { albumDataStore.setAlbums(albumList) }
        }

    @Test
    fun `Given cached album data has not expired, when getAlbum is called, then return already stored data`() =
        runTest {
            // Given
            val isManualRefresh = false
            val albumList = mockk<List<Album>>()
            val cachedAlbumList =
                mockk<CachedValue<List<Album>>> {
                    every { takeValue(isManualRefresh) } returns albumList
                }
            every { albumDataStore.getAlbums() } returns cachedAlbumList

            // When
            val result = repository.getAlbums(isManualRefresh)

            // Then
            assertEquals(
                actual = result,
                expected = albumList,
            )
            verify(inverse = true) { albumDataStore.setAlbums(any()) }
        }

    @Test
    fun `Given cached album data has expired, when getAlbum is called, then fetch data from remote, map it then store`() =
        runTest {
            // Given
            val isManualRefresh = false
            val expiredAlbumList =
                mockk<CachedValue<List<Album>>> {
                    every { takeValue(isManualRefresh) } returns null
                }
            every { albumDataStore.getAlbums() } returns expiredAlbumList

            val albumResponse = mockk<AlbumsResponse>()
            val albumList = mockk<List<Album>>()
            coEvery { albumService.getAlbums() } returns albumResponse
            every { albumMapper.toAlbums(albumResponse) } returns albumList

            // When
            val result = repository.getAlbums(isManualRefresh)

            // Then
            assertEquals(
                actual = result,
                expected = albumList,
            )
            verify { albumDataStore.setAlbums(albumList) }
        }

    @Test
    fun `Given no photos stored for an album id, when getAlbumPhotos is called, then fetch data from remote, map it then store`() =
        runTest {
            // Given
            val albumId = "album-id-1"
            val albumPhotosResponse = mockk<AlbumPhotosResponse>()
            val albumPhotoList = mockk<List<AlbumPhoto>>()
            every { albumDataStore.getAlbumPhotos(albumId) } returns null
            coEvery { albumService.getPhotos(albumId) } returns albumPhotosResponse
            every { albumPhotoMapper.toAlbumPhotos(albumPhotosResponse) } returns albumPhotoList

            // When
            val result = repository.getAlbumPhotos(albumId)

            // Then
            assertEquals(
                actual = result,
                expected = albumPhotoList,
            )
            verify {
                albumDataStore.setAlbumPhotos(
                    albumId = albumId,
                    albumPhotos = albumPhotoList,
                )
            }
        }

    @Test
    fun `Given cached album photos has not expired for an album id, when getAlbumPhotos is called, then return already stored data`() =
        runTest {
            // Given
            val albumId = "album-id-1"
            val albumPhotoList = mockk<List<AlbumPhoto>>()
            val cachedAlbumPhotoList =
                mockk<CachedValue<List<AlbumPhoto>>> {
                    every { takeValue() } returns albumPhotoList
                }
            every { albumDataStore.getAlbumPhotos(albumId) } returns cachedAlbumPhotoList

            // When
            val result = repository.getAlbumPhotos(albumId)

            // Then
            assertEquals(
                actual = result,
                expected = albumPhotoList,
            )
            verify(inverse = true) { albumDataStore.setAlbumPhotos(any(), any()) }
        }

    @Test
    fun `Given cached album photos has expired for an album, when getAlbumPhotos is called, then fetch data from remote, map it then store`() =
        runTest {
            // Given
            val albumId = "album-id-1"
            val albumPhotosResponse = mockk<AlbumPhotosResponse>()
            val albumPhotoList = mockk<List<AlbumPhoto>>()
            val cachedAlbumPhotoList =
                mockk<CachedValue<List<AlbumPhoto>>> {
                    every { takeValue() } returns null
                }
            every { albumDataStore.getAlbumPhotos(albumId) } returns cachedAlbumPhotoList
            coEvery { albumService.getPhotos(albumId) } returns albumPhotosResponse
            every { albumPhotoMapper.toAlbumPhotos(albumPhotosResponse) } returns albumPhotoList

            // When
            val result = repository.getAlbumPhotos(albumId)

            // Then
            assertEquals(
                actual = result,
                expected = albumPhotoList,
            )
            verify {
                albumDataStore.setAlbumPhotos(
                    albumId = albumId,
                    albumPhotos = albumPhotoList,
                )
            }
        }
}
