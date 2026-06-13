package com.example.albumphotos.presentation.album.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albumphotos.domain.album.FetchAlbums
import com.example.albumphotos.presentation.album.list.AlbumsNavigationAction.OpenPhotos
import com.example.albumphotos.presentation.album.list.model.AlbumUIModel
import com.example.albumphotos.presentation.album.list.model.AlbumsUIModel
import com.example.albumphotos.presentation.generic.UIState
import com.example.albumphotos.presentation.generic.event.MutableEventFlow
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AlbumsViewModel(
    private val fetchAlbums: FetchAlbums,
    private val mapper: AlbumsUIMapper,
) : ViewModel() {
    private val _albumsUIState = MutableStateFlow<UIState<AlbumsUIModel>>(UIState.Loading)
    val albumsUIState by lazy {
        getAlbums(forceRefresh = false)
        _albumsUIState.asStateFlow()
    }

    private val _navigation = MutableEventFlow<AlbumsNavigationAction>()
    val navigation = _navigation.asEventFlow()

    private val _swipeRefresh = MutableStateFlow(false)
    val swipeRefresh = _swipeRefresh.asStateFlow()

    private fun getAlbums(forceRefresh: Boolean) {
        viewModelScope.launch(Dispatchers.IO + albumsExceptionHandler) {
            _albumsUIState.value = UIState.Loading
            val albums = fetchAlbums(forceRefresh)
            _albumsUIState.value = UIState.Normal(mapper.toUIModel(albums))
            _swipeRefresh.value = false
        }
    }

    fun onAlbumClicked(albumUIModel: AlbumUIModel) {
        _navigation.setEvent(OpenPhotos(albumUIModel.id))
    }

    fun onRetryClicked() {
        _swipeRefresh.value = false
        getAlbums(forceRefresh = false)
    }

    fun onRefreshClicked() {
        _swipeRefresh.value = true
        getAlbums(forceRefresh = true)
    }

    private val albumsExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            _albumsUIState.value = UIState.Error
        }
}
