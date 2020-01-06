package com.jstarczewski.updf.db.pub

import com.jstarczewski.updf.db.Publication
import com.jstarczewski.updf.db.base.BaseDatabase
import org.ehcache.Cache
import org.ehcache.config.CacheConfigurationBuilder
import java.io.File

abstract class CachedPublicationDatabase(uploadDir: File) : BaseDatabase<Publication>(uploadDir) {

    companion object {
        private const val PUBLICATION_ALIAS = "PUBLICATION_ALIAS"
    }

    override val cache: Cache<Long, Publication> = cacheManager.createCache<Long, Publication>(
        PUBLICATION_ALIAS,
        CacheConfigurationBuilder.newCacheConfigurationBuilder<Long, Publication>().buildConfig(
            Class.forName(LONG_CLASS) as Class<Long>, Publication::class.java
        )
    )
}