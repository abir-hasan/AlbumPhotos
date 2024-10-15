package com.example.albumphotos.data.generic

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import java.lang.ref.WeakReference

class DefaultCacheRegistry : CacheRegistry {

    private val cachesRef = atomic<Set<WeakReference<Cache>>>(setOf())

    override fun register(cache: Cache) {
        cachesRef.update { it + WeakReference(cache) }
    }

    override fun clear() {
        cachesRef.update { caches ->
            for (cache in caches) cache.get()?.clear()
            caches.filterNot { it.get() == null }.toSet()
        }
    }
}
