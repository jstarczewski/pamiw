package com.jstarczewski.log.cache

import com.jstarczewski.log.publication.api.PdfResponse
import com.jstarczewski.log.publication.api.PublicationResponse
import com.jstarczewski.log.util.assign

class ResponseDataSource() : ResponseCache, ResponseCacheCreator, ResponseCacheClearer {

    private val pdfs: ArrayList<PdfResponse> = arrayListOf()
    private val pubs: ArrayList<PublicationResponse> = arrayListOf()

    override fun getPdfs(): List<PdfResponse> = pdfs

    override fun getPubs(): List<PublicationResponse> = pubs

    override fun putPdfs(pdfs: List<PdfResponse>) =
        this.pdfs.assign {
            clear()
            addAll(pdfs)
        }

    override fun putPubs(pubs: List<PublicationResponse>) =
        this.pubs.assign {
            clear()
            addAll(pubs)
        }

    override fun removePub(id: Long) {
        pubs.removeIf { it.id == id }
    }

    override fun removePdf(id: Long) {
        pdfs.removeIf { it.id == id }
    }
}