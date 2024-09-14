package com.example.albumphotos.presentation.album.photos

import app.cash.turbine.test
import com.example.albumphotos.domain.album.FetchPhotos
import com.example.albumphotos.domain.album.model.AlbumPhoto
import com.example.albumphotos.presentation.album.photos.PhotosNavigationAction.ShowPhoto
import com.example.albumphotos.presentation.album.photos.model.PhotoUIModel
import com.example.albumphotos.presentation.album.photos.model.PhotosArg
import com.example.albumphotos.presentation.album.photos.model.PhotosUIModel
import com.example.albumphotos.presentation.extension.CoroutinesExtension
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
class PhotosViewModelTest {

    @MockK
    private lateinit var photosArg: PhotosArg

    @MockK
    private lateinit var mapper: PhotosUIMapper

    @MockK
    private lateinit var fetchPhotos: FetchPhotos

    @InjectMockKs
    private lateinit var viewModel: PhotosViewModel


    @Test
    fun `Given and album id, when fetch photos is successful, then return mapped ui model in normal state`() =
        runTest {
            // Given
            val photoList = mockk<List<AlbumPhoto>>()
            val photosUIModel = mockk<PhotosUIModel>()
            val albumId = "xyz"
            every { photosArg.albumId } returns albumId
            coEvery { fetchPhotos(albumId) } coAnswers {
                delay(1)
                photoList
            }
            every { mapper.toUIModel(photoList) } returns photosUIModel

            // When
            viewModel.photosUIState.test {
                // Then
                assertEquals(awaitItem(), UIState.Loading)
                assertEquals(awaitItem(), UIState.Normal(photosUIModel))
            }
        }

    @Test
    fun `Given album id, when fetch photos throws error, then return state should be error`() =
        runTest {
            // Given
            val albumId = "xyz"
            every { photosArg.albumId } returns albumId
            coEvery { fetchPhotos(albumId) } coAnswers {
                delay(1)
                throw RuntimeException("Something went wrong")
            }

            // When
            viewModel.photosUIState.test {
                // Then
                assertEquals(awaitItem(), UIState.Loading)
                assertEquals(awaitItem(), UIState.Error)
            }
        }

    @Test
    fun `Given initial fetch photos failed, When onRetryClicked, then fetch and emit mapped photos`() = runTest {
        // Given
        val photoList = mockk<List<AlbumPhoto>>()
        val photosUIModel = mockk<PhotosUIModel>()
        val albumId = "xyz"
        every { photosArg.albumId } returns albumId
        coEvery { fetchPhotos(albumId) } answers ManyAnswersAnswer(
            listOf(
                ThrowingAnswer(RuntimeException("Something went wrong!")),
                ConstantAnswer(photoList)
            )
        )
        every { mapper.toUIModel(photoList) } returns photosUIModel

        viewModel.photosUIState.test {
            assertEquals(awaitItem(), UIState.Error)
            // When
            viewModel.onRetryClicked()
            // Then
            assertEquals(awaitItem(), UIState.Normal(photosUIModel))
        }
    }

    @Test
    fun `Given a photo, when onPhotoClicked, then open photo with the the full image url`() = runTest {
        // Given
        val url = "http://www.test.com/full/1.jpg"
        val photoUIModel = mockk<PhotoUIModel> {
            every { this@mockk.url } returns url
        }

        // When
        viewModel.onPhotoClicked(photoUIModel)

        viewModel.navigation.test {
            assertEquals(awaitItem(), Event(ShowPhoto(url)))
        }
    }
}
