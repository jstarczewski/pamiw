package com.jstarczewski.updf.responses

import com.jstarczewski.updf.db.Publication
import com.jstarczewski.updf.util.asHttpParameter
import java.io.Serializable

data class PublicationResponse(
    val id: Long,
    val author: String,
    val title: String,
    val description: String,
    val delete: Action,
    val mount: Action,
    val unmount: Action,
    val pdfs: List<PdfAction>?
) : Serializable {

    companion object {

        private const val PUB_BASE = "/pub"
        private const val LINK = "/link"
        private const val UNLINK = "/unlink"
        private const val PARAM_INFO = "/{pdf_id_long}"
        private const val PDF_BASE = "/pdf"

        fun from(publication: Publication) =
            PublicationResponse(
                publication.id,
                publication.author,
                publication.title,
                publication.description,
                Action(PUB_BASE + publication.id.asHttpParameter(), Method.DELETE.name),
                Action(PUB_BASE + publication.id.asHttpParameter() + LINK + PARAM_INFO, Method.POST.name),
                Action(PUB_BASE + publication.id.asHttpParameter() + UNLINK + PARAM_INFO, Method.POST.name),
                publication.pdfIds?.toPdfActions()
            )

        private fun List<Long>.toPdfActions() =
            this.map {
                PdfAction(
                    Action(PDF_BASE + it.asHttpParameter(), Method.GET.name),
                    Action(PDF_BASE + it.asHttpParameter(), Method.DELETE.name)
                )
            }
    }
}