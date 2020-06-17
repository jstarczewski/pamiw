package com.jstarczewski.knote.db.base

import com.jstarczewski.knote.util.getAllIds
import java.io.File
import java.util.concurrent.atomic.AtomicLong

abstract class BaseDatabase<CacheType>(private val uploadDir: File) : CacheDatabase<CacheType>() {

    protected val allIds by lazy { uploadDir.getAllIds() }

    private val biggestId by lazy { AtomicLong(allIds.max() ?: 0) }

    protected fun nextId() = biggestId.incrementAndGet()
}