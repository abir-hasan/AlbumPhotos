package com.example.albumphotos.presentation.generic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albumphotos.domain.generic.SerialJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException

context(ViewModel)
fun SerialJob.launch(block: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch {
        invoke { block() }
    }
}

context(ViewModel)
fun SerialJob.launchCatching(
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launchCatching(onError) {
        invoke { block() }
    }
}

context(ViewModel)
fun SerialJob.launchOnIO(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launchOnIO(context) {
        invoke { block() }
    }
}

context(ViewModel)
fun SerialJob.launchOnMain(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launchOnMain(context) {
        invoke { block() }
    }
}

context(ViewModel)
fun SerialJob.launchCatchingOnIO(
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launchCatchingOnIO(onError) {
        invoke { block() }
    }
}

context(ViewModel)
fun SerialJob.launchCatchingOnMain(
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewModelScope.launchCatchingOnMain(onError) {
        invoke { block() }
    }
}

/*  Main  */

fun CoroutineScope.cancelChildren() = coroutineContext.cancelChildren()

fun CoroutineScope.launchOnIO(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit,
) = launch(context = context + Dispatchers.IO, block = block)

fun CoroutineScope.launchOnMain(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit,
) = launch(context = context + Dispatchers.Main, block = block)


fun CoroutineScope.launchCatchingOnIO(
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return (this + Dispatchers.IO).launchCatching(onError, block)
}

fun CoroutineScope.launchCatchingOnMain(
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return (this + Dispatchers.Main).launchCatching(onError, block)
}


/*  Main  */

fun CoroutineScope.launchCatching(
    onError: (suspend (Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return launch {
        runCatching {
            block()
        }.onFailure {
            if (it is CancellationException) throw it else onError?.invoke(it)
        }
    }
}
