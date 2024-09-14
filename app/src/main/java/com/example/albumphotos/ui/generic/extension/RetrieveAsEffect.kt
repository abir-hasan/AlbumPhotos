package com.example.albumphotos.ui.generic.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import com.example.albumphotos.presentation.generic.event.EventFlow
import kotlinx.coroutines.launch

@Composable
fun <T : Any> EventFlow<T>.RetrieveAsEffect(
    vararg keys: Any?,
    collector: suspend (T) -> Unit,
) {
    val eventState by collectAsState(null)
    val updatedCollector by rememberUpdatedState(collector)
    val scope = rememberCoroutineScope()

    eventState?.let { event ->
        LaunchedEffect(*keys, event) {
            event.retrieve()?.let {
                scope.launch { updatedCollector(it) }
            }
        }
    }
}
