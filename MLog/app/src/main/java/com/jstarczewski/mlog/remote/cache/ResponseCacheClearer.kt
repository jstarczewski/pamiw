package com.jstarczewski.mlog.remote.cache

interface ResponseCacheClearer {

    fun removePdf(id: Long)

    fun removePub(id: Long)
}