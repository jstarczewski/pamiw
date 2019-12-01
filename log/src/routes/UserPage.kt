package com.jstarczewski.log.routes

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jstarczewski.log.*
import com.jstarczewski.log.auth.JwtConfig
import com.jstarczewski.log.db.Pdf
import com.jstarczewski.log.db.UserDataSource
import com.jstarczewski.log.util.redirect
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readBytes
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.response.respondBytes
import io.ktor.routing.Routing
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.coroutines.io.readRemaining
import kotlinx.io.core.readBytes

fun Routing.userPage(db: UserDataSource, client: HttpClient) {

    get<UserPage> {
        val user = call.sessions.get<LogSession>()?.let { db.userById(it.userId) }
        user?.let { user ->
            val r = client.get<HttpResponse>(defaultPath + "pdf/all/${user.login}") {
                header("Authorization", "Bearer ${JwtConfig.makeToken(user.login)}")
                contentType(ContentType.Application.Json)
            }
            var pdfs = emptyList<Pdf>()
            try {
                pdfs = jacksonObjectMapper().readValue<List<Pdf>>(r.readBytes(), object : TypeReference<List<Pdf>>() {})
            } catch (e: MismatchedInputException) {

            }
            call.respond(FreeMarkerContent("userpage.ftl", mapOf("all" to pdfs, "user" to user)))
        } ?: run {
            call.redirect(Index())
        }
    }

    get<UserPdf> {
        val user = call.sessions.get<LogSession>()?.let { db.userById(it.userId) }
        user?.let { user ->
            val r = client.get<HttpResponse>(defaultPath + "pdf/${it.id}") {
                header("Authorization", "Bearer ${JwtConfig.makeToken(user.login)}")
            }
            call.respondBytes(r.content.readRemaining().readBytes())
        } ?: run {
            call.redirect(Index())
        }
    }
}