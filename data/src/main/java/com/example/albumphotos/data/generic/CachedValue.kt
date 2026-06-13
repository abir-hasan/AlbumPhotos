package com.example.albumphotos.data.generic

import kotlin.time.Clock

data class CachedValue<T>(
    private val value: T,
    private val storageTimeStampMs: Long = Clock.System.now().toEpochMilliseconds(),
) {

    fun isExpired(isManualRefresh: Boolean = false): Boolean =
        Clock.System.now().toEpochMilliseconds() - storageTimeStampMs > getCacheDuration(isManualRefresh)

    fun takeValue(isManualRefresh: Boolean = false): T? = value.takeUnless { isExpired(isManualRefresh) }

    private fun getCacheDuration(isManualRefresh: Boolean): Long =
        if (isManualRefresh) MANUAL_REFRESH_CACHING_DURATION_MS else NORMAL_CACHING_DURATION_MS

    companion object {

        private const val NORMAL_CACHING_DURATION_MS = 15L * 60L * 1000L
        private const val MANUAL_REFRESH_CACHING_DURATION_MS = 15_000L
    }
}
