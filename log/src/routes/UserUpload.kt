package com.jstarczewski.log.routes

import com.jstarczewski.log.Index
import com.jstarczewski.log.UserPage
import com.jstarczewski.log.UserUpload
import com.jstarczewski.log.db.UserDataSource
import com.jstarczewski.log.defaultPath
import com.jstarczewski.log.helpers.withSession
import com.jstarczewski.log.multipart.MultiPartContent
import com.jstarczewski.log.util.auth
import com.jstarczewski.log.util.copyToSuspend
import com.jstarczewski.log.util.redirect
import com.jstarczewski.log.util.token
import io.ktor.application.call
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.Routing
import kotlinx.coroutines.io.ByteChannel
import java.io.File

private const val EMPTY_TITLE_ERROR = "Cannot upload file without title"

fun Routing.userUpload(db: UserDataSource, client: io.ktor.client.HttpClient, uploadDir: File) {

    post<UserUpload> {
        withSession(db) { user ->
            val multipart = call.receiveMultipart()
            var title: String? = null
            var pdfFile: File? = null
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
                    pdfFile = file
                }
                part.dispose()
            }
            title.takeIf { it.isNullOrBlank().not() }?.let {
                client.post<ByteChannel>(defaultPath + "upload") {
                    header(auth(), token(user.login))
                    body = MultiPartContent.build {
                        add("title", it)
                        add("file", pdfFile!!.readBytes(), filename = it)
                    }
                }
                pdfFile?.delete()
                call.redirect(UserPage())
            } ?: run {
                call.redirect(UserUpload(EMPTY_TITLE_ERROR))
            }
        } ?: run {
            call.respond(Index())
        }
    }

    get<UserUpload> {
        withSession(db) { user ->
            call.respond(
                FreeMarkerContent(
                    "upload.ftl",
                    mapOf(
                        "userId" to user.userId,
                        "user" to user,
                        "error" to it.error
                    ), ""
                )
            )
        } ?: call.respond(Index())
    }
}

