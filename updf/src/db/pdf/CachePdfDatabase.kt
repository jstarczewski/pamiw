package com.jstarczewski.updf.db.pdf

import com.jstarczewski.updf.db.Pdf
import com.jstarczewski.updf.db.base.BaseDatabase
import org.ehcache.Cache
import org.ehcache.config.CacheConfigurationBuilder
import java.io.File

abstract class CachePdfDatabase(uploadDir: File) : BaseDatabase<Pdf>(uploadDir) {

    companion object {
        private const val PDF_ALIAS = "PDF_ALIAS"
    }

    override val cache: Cache<Long, Pdf> = cacheManager.createCache<Long, Pdf>(
        PDF_ALIAS,
        CacheConfigurationBuilder.newCacheConfigurationBuilder<Long, Pdf>().buildConfig(
            Class.forName(LONG_CLASS) as Class<Long>, Pdf::class.java
        )
    )
}