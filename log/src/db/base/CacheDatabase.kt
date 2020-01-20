package com.jstarczewski.knote.db.base

import com.google.gson.GsonBuilder
import com.google.gson.LongSerializationPolicy
import org.ehcache.CacheManagerBuilder

abstract class CacheDatabase<CacheType> {

    companion object {
        const val LONG_CLASS = "java.lang.Long"
    }

    protected abstract val cache: org.ehcache.Cache<Long, CacheType>

    protected val gson = GsonBuilder()
        .disableHtmlEscaping()
        .serializeNulls()
        .setLongSerializationPolicy(LongSerializationPolicy.STRING)
        .create()

    protected val cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true)
}