package com.example.albumphotos.data.generic

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update

class StateFlowMapCache<K, V>(registry: CacheRegistry) : MapCache<K, V> {

    private val _cache = MutableStateFlow<Map<K, V>>(emptyMap())
    private val cache = _cache.asStateFlow()

    /**
     * Get the current value of the cache (synchronously).
     */
    val value
        get() = cache.value

    init {
        registry.register(this)
    }

    /**
     * Get a [Flow] that emits the current value associated with [key] and any updates to it.
     *
     * *Note: The [Flow] is [distinctUntilChanged].*
     */
    operator fun get(key: K): Flow<V?> = cache.mapLatest { it[key] }.distinctUntilChanged()

    /**
     * Get a [Flow] that emits the current values of the cache and any updates to them.
     *
     * *Note: The [Flow] is [distinctUntilChanged].*
     */
    fun values(): Flow<Collection<V>> = cache.mapLatest { it.values }

    /**
     * Get a [Flow] that emits the current keys of the cache and any updates to them.
     *
     * *Note: The [Flow] is [distinctUntilChanged].*
     */
    fun keys(): Flow<Collection<K>> = cache.mapLatest { it.keys }

    /**
     * Get a [Flow] that emits a [Map] containing all the entries in the cache and any updates to it.
     *
     * *Note: The [Flow] is [distinctUntilChanged].*
     */
    fun observe(): Flow<Map<K, V>> = cache

    /**
     * Update the value associated with [key]. Null will be passed into the lambda if value does not exist. If null is
     * returned then the [key] will be removed from the cache.
     */
    fun updateEntry(key: K, updater: (V?) -> V?) = _cache.update {
        val old = it[key]
        val new = updater(old)
        if (new != null) it + (key to new) else it - key
    }

    /**
     * Bulk update the entries in the cache in an atomic manner. If null is returned cache is cleared.
     */
    fun update(updater: (Map<K, V>) -> Map<K, V>?) = _cache.update { updater(it).orEmpty() }

    override fun plusAssign(entry: Pair<K, V>) = _cache.update { it + entry }

    override fun plusAssign(entries: Map<K, V>) = _cache.update { it + entries }

    override fun minusAssign(key: K) = _cache.update { it - key }

    override fun minusAssign(keys: Collection<K>) = _cache.update { it - keys.toSet() }

    override fun set(key: K, value: V) = _cache.update { it + (key to value) }

    override fun contains(key: K): Boolean = key in cache.value

    override fun isEmpty(): Boolean = cache.value.isEmpty()

    override fun isNotEmpty(): Boolean = cache.value.isNotEmpty()

    override fun clear() {
        _cache.value = emptyMap()
    }
}
