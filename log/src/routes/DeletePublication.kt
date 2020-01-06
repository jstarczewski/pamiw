package com.jstarczewski.log.routes

import com.jstarczewski.log.Index
import com.jstarczewski.log.UserPage
import com.jstarczewski.log.UserPublicationDelete
import com.jstarczewski.log.cache.ResponseCacheClearer
import com.jstarczewski.log.db.UserDataSource
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

fun Routing.userPublicationDelete(
    db: UserDataSource,
    client: io.ktor.client.HttpClient,
    responseCache: ResponseCacheClearer
) {

    get<UserPublicationDelete> { pipeline ->
        withSession(db) { user ->
            responseCache.getPubs().find { it.id == pipeline.id }?.let {
                client.delete<Unit> {
                    header(auth(), token(user.login))
                    url(it.delete.href)
                    responseCache.removePub(it.id)
                }
                call.redirect(UserPage())
            }
        }
        call.redirect(Index())
    }
}