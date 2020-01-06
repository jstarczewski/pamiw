package com.jstarczewski.mlog.remote.cache

import com.jstarczewski.log.publication.api.PdfResponse
import com.jstarczewski.mlog.model.publication.api.PublicationResponse

interface ResponseCache : ResponseCacheCreator, ResponseCacheClearer {

    fun getPdfs(): List<PdfResponse>

    fun getPubs(): List<PublicationResponse>
}