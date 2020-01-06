package com.jstarczewski.mlog.remote.cache

import com.jstarczewski.log.publication.api.PdfResponse
import com.jstarczewski.mlog.assign
import com.jstarczewski.mlog.model.publication.api.PublicationResponse

class ResponseDataSource private constructor() : ResponseCache {

    companion object {

        private var INSTANCE: ResponseDataSource? = null

        fun getInstance(): ResponseCache = INSTANCE ?: ResponseDataSource()
            .also {
                INSTANCE = it
            }
    }

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