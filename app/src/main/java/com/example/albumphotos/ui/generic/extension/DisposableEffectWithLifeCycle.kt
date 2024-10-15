package com.example.albumphotos.ui.generic.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Helper function to get the current lifecycle states of a composable
 * Taken from: https://stackoverflow.com/a/74780595/5219143
 */
@Composable
fun DisposableEffectWithLifecycle(
    onCreate: ((LifecycleOwner) -> Unit)? = null,
    onStart: ((LifecycleOwner) -> Unit)? = null,
    onResume: ((LifecycleOwner) -> Unit)? = null,
    onPause: ((LifecycleOwner) -> Unit)? = null,
    onStop: ((LifecycleOwner) -> Unit)? = null,
    onDestroy: ((LifecycleOwner) -> Unit)? = null,
    onAny: ((LifecycleOwner, Lifecycle.Event) -> Unit)? = null,
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    val currentOnCreate by rememberUpdatedState(onCreate)
    val currentOnStart by rememberUpdatedState(onStart)
    val currentOnResume by rememberUpdatedState(onResume)
    val currentOnPause by rememberUpdatedState(onPause)
    val currentOnStop by rememberUpdatedState(onStop)
    val currentOnDestroy by rememberUpdatedState(onDestroy)
    val currentOnAny by rememberUpdatedState(onAny)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { owner, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> currentOnCreate?.invoke(owner)
                Lifecycle.Event.ON_START -> currentOnStart?.invoke(owner)
                Lifecycle.Event.ON_RESUME -> currentOnResume?.invoke(owner)
                Lifecycle.Event.ON_PAUSE -> currentOnPause?.invoke(owner)
                Lifecycle.Event.ON_STOP -> currentOnStop?.invoke(owner)
                Lifecycle.Event.ON_DESTROY -> currentOnDestroy?.invoke(owner)
                Lifecycle.Event.ON_ANY -> currentOnAny?.invoke(owner, event)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}

@Composable
fun DisposableEffectOnCreate(onCreate: (LifecycleOwner) -> Unit) = DisposableEffectWithLifecycle(onCreate = onCreate)

@Composable
fun DisposableEffectOnStart(onStart: (LifecycleOwner) -> Unit) = DisposableEffectWithLifecycle(onStart = onStart)

@Composable
fun DisposableEffectOnResume(onResume: (LifecycleOwner) -> Unit) = DisposableEffectWithLifecycle(onResume = onResume)

@Composable
fun DisposableEffectOnPause(onPause: (LifecycleOwner) -> Unit) = DisposableEffectWithLifecycle(onPause = onPause)

@Composable
fun DisposableEffectOnStop(onStop: (LifecycleOwner) -> Unit) = DisposableEffectWithLifecycle(onStop = onStop)

@Composable
fun DisposableEffectOnDestroy(onDestroy: (LifecycleOwner) -> Unit) =
    DisposableEffectWithLifecycle(onDestroy = onDestroy)

@Composable
fun DisposableEffectOnAnyLifecycle(onAny: (LifecycleOwner, Lifecycle.Event) -> Unit) =
    DisposableEffectWithLifecycle(onAny = onAny)
