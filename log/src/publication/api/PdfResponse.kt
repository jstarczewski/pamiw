package com.jstarczewski.log.publication.api

import com.jstarczewski.log.util.applyBaseUrl
import java.io.Serializable

data class PdfResponse(
    val id: Long,
    val fileName: String,
    val author: String,
    val pdfAction: PdfAction
) : Serializable {

    companion object {

        fun toPdfResponseWithBaseUrl(pdfResponse: PdfResponse) = pdfResponse.run {
            return@run PdfResponse(
                id, fileName, author,
                PdfAction(
                    pdfAction.file.applyBaseUrl(),
                    pdfAction.delete.applyBaseUrl()
                )
            )
        }
    }
}