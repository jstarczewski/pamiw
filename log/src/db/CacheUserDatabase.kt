package com.jstarczewski.log.db

import com.jstarczewski.log.db.base.BaseDatabase
import org.ehcache.Cache
import org.ehcache.config.CacheConfigurationBuilder
import java.io.File

abstract class CacheUserDatabase(uploadDir: File) : BaseDatabase<User>(uploadDir) {

    companion object {

        private const val USER_ALIAS = "USER_ALIAS"
    }

    override val cache: Cache<Long, User> = cacheManager.createCache<Long, User>(
        USER_ALIAS,
        CacheConfigurationBuilder.newCacheConfigurationBuilder<Long, User>().buildConfig(
            Class.forName(LONG_CLASS) as Class<Long>, User::class.java
        )
    )
}