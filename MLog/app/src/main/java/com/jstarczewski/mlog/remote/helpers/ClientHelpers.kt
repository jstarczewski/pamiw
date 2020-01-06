package com.jstarczewski.mlog.remote.helpers

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jstarczewski.log.publication.api.PdfResponse
import com.jstarczewski.mlog.JwtConfig
import com.jstarczewski.mlog.auth
import com.jstarczewski.mlog.model.User
import com.jstarczewski.mlog.token
import com.jstarczewski.mlog.model.publication.api.PublicationResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readBytes
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.math.log

private const val PDFS_PATH = "pdf/all/"

private const val PUB = "pub"

const val defaultPath = "http://10.0.2.2:8080/"

private const val login = "bchaber"

private suspend fun HttpClient.buildPdfRequest(user: User) =
    get<HttpResponse>(defaultPath + PDFS_PATH + user.login) {
        header(auth(), token(login))
    }

private suspend fun HttpClient.buildPubRequest(user: User) =
    get<HttpResponse>(defaultPath + PUB) {
        header(auth(), token(login))
    }

suspend fun HttpClient.getPdfs(user: User) =
    runCatching {
        jacksonObjectMapper()
            .readValue<List<PdfResponse>>(
                buildPdfRequest(user).readBytes(),
                object : TypeReference<List<PdfResponse>>() {}
            ).map {
                PdfResponse.toPdfResponseWithBaseUrl(it)
            }
    }.getOrNull()

suspend fun HttpClient.getPublications(user: User) =
    runCatching {
        jacksonObjectMapper()
            .readValue<List<PublicationResponse>>(
                buildPubRequest(user).readBytes(),
                object : TypeReference<List<PublicationResponse>>() {}
            )
            .map {
                PublicationResponse.toPublicationResponseWithBaseUrl(it)
            }
    }.getOrNull()

suspend fun HttpClient.postWithToken(href: String, user: User) =
    post<Unit> {
        header("Authorization", "Bearer ${JwtConfig.makeToken(user.login)}")
        url(href)
        contentType(ContentType.Application.Json)
    }