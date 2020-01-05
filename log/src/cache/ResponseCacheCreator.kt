package com.jstarczewski.log.cache

import com.jstarczewski.log.publication.api.PdfResponse
import com.jstarczewski.log.publication.api.PublicationResponse

interface ResponseCacheCreator {

    fun putPdfs(pdfs: List<PdfResponse>)

    fun putPubs(pubs: List<PublicationResponse>)
}