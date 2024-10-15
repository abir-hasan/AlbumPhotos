package com.example.albumphotos.data.generic

interface MapCache<K, V> : Cache {

    /**
     * See [MutableMap.plusAssign]
     */
    operator fun plusAssign(entry: Pair<K, V>)

    /**
     * See [MutableMap.plusAssign]
     */
    operator fun plusAssign(entries: Map<K, V>)

    /**
     * See [MutableMap.minusAssign]
     */
    operator fun minusAssign(key: K)

    /**
     * See [MutableMap.minusAssign]
     */
    operator fun minusAssign(keys: Collection<K>)

    /**
     * See [MutableMap.set]
     */
    operator fun set(key: K, value: V)

    /**
     * See [MutableMap.contains]
     */
    operator fun contains(key: K): Boolean

    /**
     * See [MutableMap.isEmpty]
     */
    fun isEmpty(): Boolean

    /**
     * See [MutableMap.isNotEmpty]
     */
    fun isNotEmpty(): Boolean
}
