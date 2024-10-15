package com.example.albumphotos.data.generic

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StateFlowValueCache<T>(
    registry: CacheRegistry,
) : ValueCache<T> {

    private val _cache: MutableStateFlow<T?> = MutableStateFlow(null)
    private val cache: StateFlow<T?> = _cache.asStateFlow()

    init {
        registry.register(this)
    }

    override val value: T?
        get() = cache.value

    override fun clear() {
        _cache.value = null
    }

    fun get(): Flow<T?> = cache

    override fun put(data: T) {
        _cache.value = data
    }

    override fun update(updater: (T?) -> T?) = _cache.update(updater)

    companion object
}
