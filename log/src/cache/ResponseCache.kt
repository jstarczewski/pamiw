package com.jstarczewski.log.cache

import com.jstarczewski.log.publication.api.PdfResponse
import com.jstarczewski.log.publication.api.PublicationResponse

interface ResponseCache {

    fun getPdfs() : List<PdfResponse>

    fun getPubs() : List<PublicationResponse>
}