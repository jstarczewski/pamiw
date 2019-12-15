package com.jstarczewski.updf

import com.jstarczewski.updf.db.pdf.PdfDataSource
import com.jstarczewski.updf.db.pub.PublicationDataSource
import com.jstarczewski.updf.responses.PublicationResponse
import com.jstarczewski.updf.util.toLongList
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.locations.delete
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing

private const val AUTHOR = "author"
private const val TITLE = "title"
private const val DESCRIPTION = "description"
private const val PDF_IDS = "pdfIds"

fun Routing.publication(pdfDataSource: PdfDataSource, publicationDatasource: PublicationDataSource) {

    post<Publications> {
        val post = call.receive<Parameters>()
        val author = post[AUTHOR]
        val title = post[TITLE]
        val description = post[DESCRIPTION]
        val pdfIds = post[PDF_IDS]
        if (author != null && title != null && description != null && pdfIds != null) {
            val publicationId = publicationDatasource.savePublication(author, title, description, pdfIds.toLongList())
            call.respond(publicationId)
        } else {
            call.respond(HttpStatusCode.UnprocessableEntity)
        }
    }

    get<Publication> {
        publicationDatasource.getAllPublications().find { publication -> publication.id == it.id }?.run {
            call.respond(PublicationResponse.from(this))
        } ?: run {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    delete<Publication> {
        val deleteResult = publicationDatasource.deletePublication(it.id)
        if (deleteResult) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    post<Link> {
        publicationDatasource.getPublicationById(it.pubId)?.let { publication ->
            pdfDataSource.getPdfById(it.pdfId)?.let { pdf ->
                val result = publicationDatasource.linkPdfWithPublication(it.pdfId, publication.id)
                if (result) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } ?: run {
                call.respond(HttpStatusCode.NotFound, "Pdf with given ID not found")
            }
        } ?: run {
            call.respond(HttpStatusCode.NotFound, "Publication with given ID not found")
        }
    }

    post<Unlink> {
        publicationDatasource.getPublicationById(it.pubId)?.let { publication ->
            pdfDataSource.getPdfById(it.pdfId)?.let { pdf ->
                val result = publicationDatasource.unlinkPdfWithPublication(pdf.id, publication.id)
                if (result) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } ?: run {
                call.respond(HttpStatusCode.NotFound, "Pdf with given ID not found")
            }
        } ?: run {
            call.respond(HttpStatusCode.NotFound, "Publication with given ID not found")
        }
    }
}