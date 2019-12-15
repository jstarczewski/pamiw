package com.jstarczewski.updf.db.base

import com.google.gson.GsonBuilder
import com.google.gson.LongSerializationPolicy
import org.ehcache.Cache
import org.ehcache.CacheManagerBuilder

abstract class CacheDatabase<CacheType> {

    companion object {
        const val LONG_CLASS = "java.lang.Long"
    }

    protected abstract val cache: Cache<Long, CacheType>

    protected val gson = GsonBuilder()
        .disableHtmlEscaping()
        .serializeNulls()
        .setLongSerializationPolicy(LongSerializationPolicy.STRING)
        .create()

    protected val cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true)
}