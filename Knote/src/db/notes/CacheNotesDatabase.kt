package com.jstarczewski.knote.db.notes

import com.jstarczewski.knote.db.base.BaseDatabase
import com.jstarczewski.knote.db.model.Note
import org.ehcache.Cache
import org.ehcache.config.CacheConfigurationBuilder
import java.io.File

abstract class CacheNotesDatabase(uploadDir: File) : BaseDatabase<Note>(uploadDir) {

    companion object {

        private const val NOTE_ALIAS = "NOTE_ALIAS"
    }

    override val cache: Cache<Long, Note> = cacheManager.createCache<Long, Note>(
        NOTE_ALIAS,
        CacheConfigurationBuilder.newCacheConfigurationBuilder<Long, Note>().buildConfig(
            Class.forName(LONG_CLASS) as Class<Long>, Note::class.java
        )
    )
}