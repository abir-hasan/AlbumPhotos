package com.example.albumphotos.data.generic

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update

class SimpleMapCache<K, V>(registry: CacheRegistry) : MapCache<K, V> {

    private val ref = atomic<Map<K, V>>(emptyMap())

    val values: Collection<V>
        get() = ref.value.values

    val keys: Collection<K>
        get() = ref.value.keys

    init {
        registry.register(this)
    }

    override fun clear() {
        ref.value = emptyMap()
    }

    override fun plusAssign(entry: Pair<K, V>) = ref.update { it + entry }

    override fun plusAssign(entries: Map<K, V>) = ref.update { it + entries }

    override fun minusAssign(key: K) = ref.update { it - key }

    override fun minusAssign(keys: Collection<K>) = ref.update { it - keys.toSet() }

    override fun set(key: K, value: V) = ref.update { it + (key to value) }

    /**
     * See [Map.get]
     */
    operator fun get(key: K) = ref.value[key]

    /**
     * See [Map.getValue]
     */
    fun getValue(key: K) = ref.value.getValue(key)

    override fun contains(key: K) = key in ref.value

    override fun isEmpty() = ref.value.isEmpty()

    override fun isNotEmpty() = ref.value.isNotEmpty()
}
