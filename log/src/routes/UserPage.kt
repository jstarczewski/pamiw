package com.jstarczewski.log.routes

import com.jstarczewski.log.*
import com.jstarczewski.log.auth.JwtConfig
import com.jstarczewski.log.cache.ResponseCacheCreator
import com.jstarczewski.log.db.UserDataSource
import com.jstarczewski.log.helpers.getPdfs
import com.jstarczewski.log.helpers.getPublications
import com.jstarczewski.log.helpers.withSession
import com.jstarczewski.log.util.Notifier
import com.jstarczewski.log.util.redirect
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.response.HttpResponse
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.response.respondBytes
import io.ktor.routing.Routing
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.io.readRemaining
import kotlinx.io.core.readBytes

@ExperimentalCoroutinesApi
fun Routing.userPage(
    db: UserDataSource,
    client: HttpClient,
    responseCacheCreator: ResponseCacheCreator,
    notifier: Notifier
) {

    get<UserPage> {
        withSession(db) { user ->
            val pdfs = client.getPdfs(user)
                ?.apply {
                    responseCacheCreator.putPdfs(this)
                } ?: emptyList()
            val publications = client.getPublications(user)
                ?.apply {
                    responseCacheCreator.putPubs(this)
                } ?: emptyList()
            call.sessions.get<LogSession>()?.let {
                notifier.users.add(it)
            }
            call.respond(
                FreeMarkerContent(
                    "userpage.ftl",
                    mapOf("all" to pdfs, "all_pubs" to publications, "user" to user, "error" to it.error)
                )
            )
        } ?: run {
            call.redirect(Index())
        }
    }

    get<UserPdf> {
        withSession(db) { user ->
            val r = client.get<HttpResponse>(defaultPath + "pdf/${it.id}") {
                header("Authorization", "Bearer ${JwtConfig.makeToken(user.login)}")
            }
            call.respondBytes(r.content.readRemaining().readBytes())
        } ?: run {
            call.redirect(Index())
        }
    }
}