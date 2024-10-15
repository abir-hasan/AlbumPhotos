package com.example.albumphotos.data.generic

interface ValueCache<T> : Cache {

    val value: T?

    fun put(data: T)

    fun update(updater: (T?) -> T?)
}
