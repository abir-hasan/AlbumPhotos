package com.example.albumphotos.presentation.album.list

import app.cash.turbine.test
import com.example.albumphotos.domain.album.FetchAlbums
import com.example.albumphotos.domain.album.model.Album
import com.example.albumphotos.presentation.album.list.model.AlbumUIModel
import com.example.albumphotos.presentation.album.list.model.AlbumsUIModel
import com.example.albumphotos.core.test.CoroutinesExtension
import com.example.albumphotos.presentation.generic.UIState
import com.example.albumphotos.presentation.generic.event.Event
import io.mockk.ConstantAnswer
import io.mockk.ManyAnswersAnswer
import io.mockk.ThrowingAnswer
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class, CoroutinesExtension::class)
class AlbumsViewModelTest {

    @MockK
    private lateinit var fetchAlbums: FetchAlbums

    @MockK
    private lateinit var mapper: AlbumsUIMapper

    @InjectMockKs
    private lateinit var viewModel: AlbumsViewModel


    @Test
    fun `Given fetch albums is successful, when getAlbums called, then return mapped ui model in normal state`() =
        runTest {
            // Given
            val albumList = mockk<List<Album>>()
            val albumsUIModel = mockk<AlbumsUIModel>()
            coEvery { fetchAlbums(false) } coAnswers {
                delay(1) // Simulating network
                albumList
            }
            every { mapper.toUIModel(albumList) } returns albumsUIModel

            // When
            viewModel.albumsUIState.test {
                // Then
                assertEquals(awaitItem(), UIState.Loading)
                assertEquals(awaitItem(), UIState.Normal(albumsUIModel))
            }
        }

    @Test
    @Suppress("TooGenericExceptionThrown")
    fun `Given fetch albums throws error, when getAlbums called, then return state should be error`() = runTest {
        // Given
        coEvery { fetchAlbums(false) } coAnswers {
            delay(1)
            throw RuntimeException("Something went wrong")
        }

        // When
        viewModel.albumsUIState.test {
            // Then
            assertEquals(awaitItem(), UIState.Loading)
            assertEquals(awaitItem(), UIState.Error)
        }
    }

    @Test
    fun `Given initial fetch album failed, When onRetryClicked, then fetch and emit mapped albums`() = runTest {
        // Given
        val albumList = mockk<List<Album>>()
        val albumsUIModel = mockk<AlbumsUIModel>()
        coEvery { fetchAlbums(false) } answers ManyAnswersAnswer(
            listOf(
                ThrowingAnswer(RuntimeException("Something went wrong!")),
                ConstantAnswer(albumList)
            )
        )
        every { mapper.toUIModel(albumList) } returns albumsUIModel

        viewModel.albumsUIState.test {
            assertEquals(awaitItem(), UIState.Error)
            // When
            viewModel.onRetryClicked()
            // Then
            assertEquals(awaitItem(), UIState.Normal(albumsUIModel))
        }
    }

    @Test
    fun `Given an album, when onAlbumClicked, then open photos with the the album id`() = runTest {
        // Given
        val albumId = "123"
        val album = mockk<AlbumUIModel> {
            every { this@mockk.id } returns albumId
        }

        // When
        viewModel.onAlbumClicked(album)

        viewModel.navigation.test {
            assertEquals(awaitItem(), Event(AlbumsNavigationAction.OpenPhotos(albumId)))
        }
    }
}
