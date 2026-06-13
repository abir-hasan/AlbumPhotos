package com.example.albumphotos.core.test

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import kotlin.coroutines.CoroutineContext

open class CoroutinesExtension : TestExtension(), BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private val testDispatcher = UnconfinedTestDispatcher()

    override fun beforeTestExecution(context: ExtensionContext) {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Dispatchers::class)
        every { Dispatchers.Main } answers { callOriginal() }
        every { Dispatchers.IO } answers { testDispatcher }
        every { Dispatchers.Unconfined } answers { testDispatcher }
        every { Dispatchers.Default } answers { testDispatcher }
        every { Dispatchers.Default.fold<Any>(any(), any()) } answers { testDispatcher.fold(firstArg(), secondArg()) }
        every {
            Dispatchers.Default.plus(any<CoroutineContext>())
        } answers { testDispatcher.plus(firstArg<CoroutineContext>()) }
    }

    override fun afterTestExecution(context: ExtensionContext) {
        unmockkStatic(Dispatchers::class)
        Dispatchers.resetMain()
    }
}
