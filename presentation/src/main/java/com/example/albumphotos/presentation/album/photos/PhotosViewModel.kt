package com.example.albumphotos.presentation.album.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albumphotos.domain.album.FetchPhotos
import com.example.albumphotos.presentation.album.photos.PhotosNavigationAction.ShowPhoto
import com.example.albumphotos.presentation.album.photos.model.PhotoUIModel
import com.example.albumphotos.presentation.album.photos.model.PhotosArg
import com.example.albumphotos.presentation.album.photos.model.PhotosUIModel
import com.example.albumphotos.presentation.generic.UIState
import com.example.albumphotos.presentation.generic.event.MutableEventFlow
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class PhotosViewModel(
    @InjectedParam private val photosArg: PhotosArg,
    private val fetchPhotos: FetchPhotos,
    private val mapper: PhotosUIMapper,
) : ViewModel() {

    private val _photosUIState = MutableStateFlow<UIState<PhotosUIModel>>(UIState.Loading)
    val photosUIState by lazy {
        getPhotos()
        _photosUIState.asStateFlow()
    }

    private val _navigation = MutableEventFlow<PhotosNavigationAction>()
    val navigation = _navigation.asEventFlow()

    private fun getPhotos() {
        viewModelScope.launch(Dispatchers.IO + photosExceptionHandler) {
            _photosUIState.value = UIState.Loading
            val photos = fetchPhotos(photosArg.albumId)
            _photosUIState.value = UIState.Normal(mapper.toUIModel(photos))
        }
    }

    fun onRetryClicked() = getPhotos()

    fun onPhotoClicked(photoUIModel: PhotoUIModel) {
        _navigation.setEvent(ShowPhoto(photoUIModel.url))
    }

    private val photosExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            _photosUIState.value = UIState.Error
        }
}
