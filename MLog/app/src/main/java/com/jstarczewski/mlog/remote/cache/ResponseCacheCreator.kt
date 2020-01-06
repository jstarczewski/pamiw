package com.jstarczewski.mlog.remote.cache

import com.jstarczewski.log.publication.api.PdfResponse
import com.jstarczewski.mlog.model.publication.api.PublicationResponse

interface ResponseCacheCreator {

    fun putPdfs(pdfs: List<PdfResponse>)

    fun putPubs(pubs: List<PublicationResponse>)
}