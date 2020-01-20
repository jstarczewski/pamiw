package com.jstarczewski.log.routes

import com.jstarczewski.log.db.UserDataSource
import com.jstarczewski.log.DeletePdf
import com.jstarczewski.log.UserPage
import com.jstarczewski.log.cache.ResponseCacheClearer
import com.jstarczewski.log.helpers.withSession
import com.jstarczewski.log.util.auth
import com.jstarczewski.log.util.redirect
import com.jstarczewski.log.util.token
import io.ktor.application.call
import io.ktor.client.request.delete
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.locations.get
import io.ktor.routing.Routing


fun Routing.userPdfDelete(db: UserDataSource, client: io.ktor.client.HttpClient, responseCache: ResponseCacheClearer) {

    get<DeletePdf> {
        val id = it.id
        withSession(db) { user ->
            responseCache.getPdfs().find { it.id == id }?.let {
                client.delete<Unit> {
                    header(auth(), token(user.login))
                    url(it.pdfAction.delete.href)
                    responseCache.removePdf(it.id)
                }
            }
        }
        call.redirect(UserPage())
    }
}