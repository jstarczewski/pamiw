package com.jstarczewski.updf.responses

import com.jstarczewski.updf.db.Pdf
import com.jstarczewski.updf.util.asHttpParameter
import java.io.Serializable

data class PdfResponse(
    val id: Long,
    val fileName: String,
    val author: String,
    val pdfAction: PdfAction
) : Serializable {

    companion object {

        private const val PDF_BASE = "/pdf"

        fun from(pdf: Pdf) = PdfResponse(
            pdf.id,
            pdf.fileName,
            pdf.author,
            pdf.id.toPdfActions()
        )

        private fun Long.toPdfActions() =
            PdfAction(
                Action(PDF_BASE + this.asHttpParameter(), Method.GET.name),
                Action(PDF_BASE + this.asHttpParameter(), Method.DELETE.name)
            )
    }
}