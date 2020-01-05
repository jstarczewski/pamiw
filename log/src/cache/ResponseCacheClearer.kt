package com.jstarczewski.log.cache

interface ResponseCacheClearer : ResponseCache {

    fun removePdf(id: Long)

    fun removePub(id: Long)
}