package com.example.albumphotos.ui.album.list.previewprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.albumphotos.presentation.album.list.model.AlbumUIModel
import com.example.albumphotos.presentation.album.list.model.AlbumsUIModel
import com.example.albumphotos.presentation.generic.UIState
import kotlinx.collections.immutable.toImmutableList

class AlbumsPreviewParameterProvider : PreviewParameterProvider<UIState<AlbumsUIModel>> {
    override val values: Sequence<UIState<AlbumsUIModel>>
        get() =
            sequenceOf(
                UIState.Normal(
                    AlbumsUIModel(
                        albums =
                            listOf(
                                AlbumUIModel(
                                    id = "1",
                                    title = "Here is a random",
                                    firstLetter = "T",
                                ),
                                AlbumUIModel(
                                    id = "2",
                                    title = "Here is a random album title",
                                    firstLetter = "T",
                                ),
                                AlbumUIModel(
                                    id = "3",
                                    title = "Here is a random album title",
                                    firstLetter = "T",
                                ),
                                AlbumUIModel(
                                    id = "4",
                                    title = "Here is a random album title",
                                    firstLetter = "T",
                                ),
                                AlbumUIModel(
                                    id = "5",
                                    title = "Here is a random",
                                    firstLetter = "T",
                                ),
                            ).toImmutableList(),
                    ),
                ),
                UIState.Error,
                UIState.Loading,
            )
}
