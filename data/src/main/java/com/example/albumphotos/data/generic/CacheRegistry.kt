package com.example.albumphotos.data.generic

interface CacheRegistry {

    fun register(cache: Cache)
    fun clear()
}
