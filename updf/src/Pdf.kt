package com.jstarczewski.updf

import com.jstarczewski.updf.auth.User
import com.jstarczewski.updf.db.pdf.PdfDataSource
import com.jstarczewski.updf.db.pub.PublicationDataSource
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.fromFilePath
import io.ktor.locations.delete
import io.ktor.locations.get
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.response.respondFile
import io.ktor.routing.Routing
import java.io.File

private const val path = ".updf-files/"
private const val extension = ".pdf"

fun Routing.pdf(pdfDataSource: PdfDataSource, publicationDataSource: PublicationDataSource) {

    authenticate {
        get<Pdf> {
            call.principal<User>()?.userName?.run {
                pdfDataSource.getPdfById(it.id)?.let { pdf ->
                    ContentType.fromFilePath(pdf.fileName + extension).firstOrNull()?.let {
                        call.response.header("Content-Disposition", "attachment; filename=\"${pdf.fileName}.pdf\"")
                        call.respondFile(File(path + pdf.fileName + extension))
                    }
                } ?: call.respond(HttpStatusCode.Forbidden)
            }
        }

        get<UserPdf> {
            call.principal<User>()?.userName?.run {
                pdfDataSource.getAllPdfFiles()
                    .filter { pdf -> pdf.author == this }
                    .takeIf { it.count() != 0 }
                    ?.run {
                        call.respond(this.toList())
                    } ?: call.respond(HttpStatusCode.NotFound)
            } ?: call.respond(HttpStatusCode.Forbidden)
        }
    }

    delete<Pdf> {
        val pdfDeleteResult = pdfDataSource.deletePdf(it.id)
        publicationDataSource.getAllPublications().forEach { publication ->
            publicationDataSource.unlinkPdfWithPublication(it.id, publication.id)
        }
        if (pdfDeleteResult) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}