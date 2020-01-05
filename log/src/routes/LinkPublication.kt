package com.jstarczewski.log.routes

import com.jstarczewski.log.LinkPublication
import com.jstarczewski.log.UserPage
import com.jstarczewski.log.cache.ResponseCache
import com.jstarczewski.log.db.UserDataSource
import com.jstarczewski.log.helpers.postWithToken
import com.jstarczewski.log.helpers.withSession
import com.jstarczewski.log.util.redirect
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.http.Parameters
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.routing.Routing

private const val FILE_NOT_FOUND_ERROR = "File with given name does not exist"

private const val FILE_NAME = "fileName"

fun Routing.linkPublication(db: UserDataSource, client: HttpClient, responseCache: ResponseCache) {

    post<LinkPublication> {
        withSession(db) { user ->
            responseCache.getPubs().find { pub -> pub.id == it.id }?.run {
                call.receive<Parameters>()[FILE_NAME]?.let {
                    val name = it
                    val pdf = responseCache.getPdfs().find { pdf -> pdf.fileName == name }
                    pdf?.id?.let { pdfId ->
                        client.postWithToken("${mount.href}${pdfId}", user)
                        call.redirect(UserPage())
                    }
                }
            }
        }
        call.redirect(UserPage(FILE_NOT_FOUND_ERROR))
    }
}