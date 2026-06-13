package com.example.albumphotos.presentation.generic

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface UIState<out D> {

    @Immutable
    data class Normal<out T>(
        val data: T,
    ) : UIState<T>

    @Immutable
    data object Loading : UIState<Nothing>

    @Immutable
    data object Error : UIState<Nothing>
}
