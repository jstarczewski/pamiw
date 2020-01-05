package com.jstarczewski.log.db

import com.jstarczewski.log.publication.api.PdfResponse
import java.io.Serializable

data class Pdf(val id: Long, val fileName: String, val author: String) : Serializable {

    companion object {

        fun from(pdfResponse: PdfResponse) = Pdf(pdfResponse.id, pdfResponse.fileName, pdfResponse.author)
    }
}