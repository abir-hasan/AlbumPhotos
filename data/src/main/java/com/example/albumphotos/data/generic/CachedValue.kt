package com.example.albumphotos.data.generic

import kotlinx.datetime.Clock

data class CachedValue<T>(
    private val value: T,
    private val storageTimeStampMs: Long = Clock.System.now().toEpochMilliseconds(),
) {

    fun isExpired(isManualRefresh: Boolean = false): Boolean {
        return Clock.System.now().toEpochMilliseconds() - storageTimeStampMs > getCacheDuration(isManualRefresh)
    }

    fun takeValue(isManualRefresh: Boolean = false): T? {
        return value.takeUnless { isExpired(isManualRefresh) }
    }

    private fun getCacheDuration(isManualRefresh: Boolean): Long {
        return if (isManualRefresh) ManualRefreshCachingDurationMs else NormalCachingDurationMs
    }

    companion object {

        private const val NormalCachingDurationMs = 15L * 60L * 1000L
        private const val ManualRefreshCachingDurationMs = 15_000L
    }
}
