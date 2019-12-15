package com.jstarczewski.updf

import com.jstarczewski.updf.db.pdf.PdfDataSource
import com.jstarczewski.updf.util.copyToSuspend
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.locations.post
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.Route
import java.io.File
import kotlin.random.Random

const val TEST_USER_ID = "bchaber"

fun Route.upload(dataSource: PdfDataSource, uploadDir: File) {

    authenticate {
        post<Upload> {
            val multipart = call.receiveMultipart()
            var title = ""
            var pdfFile: File? = null

            multipart.forEachPart { part ->
                if (part is PartData.FormItem) {
                    if (part.name == "title") {
                        title = part.value
                    }
                } else if (part is PartData.FileItem) {
                    val pdfs = dataSource.getAllPdfFiles().toList()
                    if (pdfs.isEmpty().not()) {
                        pdfs.find { it.fileName == title }?.let {
                            title += Random(title.hashCode()).toString().hashCode()
                        }
                    }
                    val file = File(
                        uploadDir,
                        "$title.pdf"
                    )
                    part.streamProvider()
                        .use { its -> file.outputStream().buffered().use { its.copyToSuspend(it) } }
                    pdfFile = file
                }
                part.dispose()
            }
            pdfFile?.let {
                dataSource.savePdf(TEST_USER_ID, title)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
