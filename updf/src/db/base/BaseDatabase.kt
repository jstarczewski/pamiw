package com.jstarczewski.updf.db.base

import com.jstarczewski.updf.util.getAllIds
import java.io.File
import java.util.concurrent.atomic.AtomicLong

abstract class BaseDatabase<CacheType>(private val uploadDir: File) : CacheDatabase<CacheType>() {

    protected val allIds by lazy { uploadDir.getAllIds() }

    protected val biggestId by lazy { AtomicLong(allIds.max() ?: 0) }

    protected fun nextId() = biggestId.incrementAndGet()
}