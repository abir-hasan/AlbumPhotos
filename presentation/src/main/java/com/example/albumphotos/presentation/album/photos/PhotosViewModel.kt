package com.example.albumphotos.presentation.album.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albumphotos.domain.album.FetchPhotos
import com.example.albumphotos.presentation.album.photos.model.PhotosArg
import com.example.albumphotos.presentation.album.photos.model.PhotosUIModel
import com.example.albumphotos.presentation.generic.UIState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

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

    private fun getPhotos() {
        viewModelScope.launch(Dispatchers.IO + photosExceptionHandler) {
            _photosUIState.value = UIState.Loading
            val photos = fetchPhotos(photosArg.albumId)
            _photosUIState.value = UIState.Normal(mapper.toUIModel(photos))
        }
    }

    fun onRetryClicked() = getPhotos()

    private val photosExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _photosUIState.value = UIState.Error
    }
}
