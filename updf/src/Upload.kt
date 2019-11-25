package com.jstarczewski.updf

import com.jstarczewski.updf.db.PdfDataSource
import io.ktor.application.call
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.locations.post
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.Route
import java.io.File

const val TEST_USER_ID = "janol"

fun Route.upload(dataSource: PdfDataSource, uploadDir: File) {

    post<Upload> {
        val multipart = call.receiveMultipart()
        var title = ""
        var videoFile: File? = null

        multipart.forEachPart { part ->
            if (part is PartData.FormItem) {
                if (part.name == "title") {
                    title = part.value
                }
            } else if (part is PartData.FileItem) {
                val ext = File(part.originalFileName).extension
                val file = File(
                    uploadDir,
                    "$title.$ext"
                )

                part.streamProvider().use { its -> file.outputStream().buffered().use { its.copyToSuspend(it) } }
                videoFile = file
            }

            part.dispose()
        }
        videoFile?.let {
            val id = dataSource.savePdf(title, TEST_USER_ID, it)
            call.respond(id)
        }
    }
}
