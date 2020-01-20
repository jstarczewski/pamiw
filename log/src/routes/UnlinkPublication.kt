package com.jstarczewski.log.routes

import com.jstarczewski.log.db.UserDataSource
import com.jstarczewski.log.LogSession
import com.jstarczewski.log.UnlinkPublication
import com.jstarczewski.log.UserPage
import com.jstarczewski.log.cache.ResponseCache
import com.jstarczewski.log.util.auth
import com.jstarczewski.log.util.redirect
import com.jstarczewski.log.util.token
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.Parameters
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.routing.Routing
import io.ktor.sessions.get
import io.ktor.sessions.sessions

private const val FILE_NOT_FOUND_ERROR = "File with given name does not exist"

fun Routing.unlinkPublication(db: UserDataSource, client: HttpClient, responseCache: ResponseCache) {

    post<UnlinkPublication> {
        val user = call.sessions.get<LogSession>()?.let { db.userById(it.userId.toLong()) }
        val pubId = it.id
        val pdfs = responseCache.getPdfs()
        val publications = responseCache.getPubs()
        publications.find { publicationResponse -> publicationResponse.id == pubId }?.run {
            user?.let {
                val params = call.receive<Parameters>()
                val fileName = params["fileName"]
                fileName?.let {
                    val name = it
                    val pdf = pdfs.find { pdf -> pdf.fileName == name }
                    pdf?.id?.let { pdfId ->
                        client.post<Unit> {
                            header(auth(), token(user.login))
                            url("${unmount.href}${pdfId}")
                        }
                        call.redirect(UserPage())
                    }
                }
            }
        } ?: run {
            call.redirect(UserPage(FILE_NOT_FOUND_ERROR))
        }
    }
}