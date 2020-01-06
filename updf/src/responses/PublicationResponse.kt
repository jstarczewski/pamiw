package com.jstarczewski.updf.responses

import com.jstarczewski.updf.db.Pdf
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
    val pdfs: List<PdfResponse>?
) : Serializable {

    companion object {

        private const val PUB_BASE = "/pub"
        private const val LINK = "/link"
        private const val UNLINK = "/unlink"
        private const val EXPECT_PARAMETER = "/"

        fun from(publication: Publication, pdfs: List<Pdf>?) =
            PublicationResponse(
                publication.id,
                publication.author,
                publication.title,
                publication.description,
                Action(PUB_BASE + publication.id.asHttpParameter(), Method.DELETE.name),
                Action(PUB_BASE + publication.id.asHttpParameter() + LINK + EXPECT_PARAMETER, Method.POST.name),
                Action(PUB_BASE + publication.id.asHttpParameter() + UNLINK + EXPECT_PARAMETER, Method.POST.name),
                pdfs?.map { PdfResponse.from(it) }
            )
    }
}