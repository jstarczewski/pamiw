package com.jstarczewski.log.helpers

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jstarczewski.log.auth.JwtConfig
import com.jstarczewski.log.db.User
import com.jstarczewski.log.defaultPath
import com.jstarczewski.log.publication.api.PdfResponse
import com.jstarczewski.log.publication.api.PublicationResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readBytes
import io.ktor.http.ContentType
import io.ktor.http.contentType

private const val PDFS_PATH = "pdf/all/"

private const val PUB = "pub"

private suspend fun HttpClient.buildPdfRequest(user: User) =
    get<HttpResponse>(defaultPath + PDFS_PATH + user.login) {
        header("Authorization", "Bearer ${JwtConfig.makeToken(user.login)}")
        contentType(ContentType.Application.Json)
    }

private suspend fun HttpClient.buildPubRequest(user: User) =
    get<HttpResponse>(defaultPath + PUB) {
        header("Authorization", "Bearer ${JwtConfig.makeToken(user.login)}")
        contentType(ContentType.Application.Json)
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
    }