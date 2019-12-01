package com.jstarczewski.updf.db.base

import com.google.gson.GsonBuilder
import com.google.gson.LongSerializationPolicy
import com.jstarczewski.updf.db.Pdf
import org.ehcache.Cache
import org.ehcache.CacheManagerBuilder
import org.ehcache.config.CacheConfigurationBuilder

abstract class CachePdfDatabase {

    companion object {
        private const val ALIAS = "alias"
        private const val LONG_CLASS = "java.lang.Long"
    }

    protected val gson = GsonBuilder()
        .disableHtmlEscaping()
        .serializeNulls()
        .setLongSerializationPolicy(LongSerializationPolicy.STRING)
        .create()

    private val cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true)

    protected val pdfCache: Cache<Long, Pdf> = cacheManager.createCache<Long, Pdf>(
        ALIAS,
        CacheConfigurationBuilder.newCacheConfigurationBuilder<Long, Pdf>().buildConfig(
            Class.forName(LONG_CLASS) as Class<Long>, Pdf::class.java
        )
    )
}