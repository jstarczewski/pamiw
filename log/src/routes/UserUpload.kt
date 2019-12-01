package com.jstarczewski.log.routes

import com.jstarczewski.log.LogSession
import com.jstarczewski.log.UserPage
import com.jstarczewski.log.UserUpload
import com.jstarczewski.log.auth.JwtConfig
import com.jstarczewski.log.db.UserDataSource
import com.jstarczewski.log.defaultPath
import com.jstarczewski.log.multipart.MultiPartContent
import com.jstarczewski.log.util.copyToSuspend
import com.jstarczewski.log.util.redirect
import io.ktor.application.call
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.coroutines.io.ByteChannel
import java.io.File

fun Routing.userUpload(db: UserDataSource, client: io.ktor.client.HttpClient, uploadDir: File) {

    val port = 8080

    post<UserUpload> {
        call.sessions.get<LogSession>()?.let { db.userById(it.userId) }?.let { user ->
            val multipart = call.receiveMultipart()
            var title = ""
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
            client.post<ByteChannel>(defaultPath + "upload") {
                header("Authorization", "Bearer ${JwtConfig.makeToken(user.login)}")
                body = MultiPartContent.build {
                    add("title", title)
                    add("file", pdfFile!!.readBytes(), filename = title)
                }
            }
            pdfFile?.delete()
            call.redirect(UserPage())
        } ?: kotlin.run {
            call.respond(HttpStatusCode.Forbidden)
        }
    }

    get<UserUpload> {
        call.sessions.get<LogSession>()?.let { db.userById(it.userId) }?.let {
            call.respond(FreeMarkerContent("upload.ftl", mapOf("userId" to it.userId, "user" to it), ""))
        } ?: call.respond(HttpStatusCode.Forbidden)
    }
}

