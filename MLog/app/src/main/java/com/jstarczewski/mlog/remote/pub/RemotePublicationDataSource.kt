package com.jstarczewski.mlog.remote.pub

import com.jstarczewski.mlog.auth
import com.jstarczewski.mlog.model.User
import com.jstarczewski.mlog.model.publication.api.PublicationResponse
import com.jstarczewski.mlog.remote.cache.ResponseCache
import com.jstarczewski.mlog.remote.helpers.defaultPath
import com.jstarczewski.mlog.remote.helpers.getPublications
import com.jstarczewski.mlog.remote.helpers.postWithToken
import com.jstarczewski.mlog.token
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

class RemotePublicationDataSource private constructor(
    private val client: HttpClient,
    private val cache: ResponseCache
) : PublicationDataSource {

    companion object {

        private const val AUTHOR = "author"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val PDF_IDS = "pdfIds"

        private var INSTANCE: PublicationDataSource? = null

        fun getInstance(client: HttpClient, cache: ResponseCache) = INSTANCE
            ?: RemotePublicationDataSource(client, cache).also {
                INSTANCE = it
            }
    }

    override suspend fun addPublication(title: String, description: String, user: User) {
        client.submitForm<Unit> {
            header(auth(), token(user.login))
            url("${defaultPath}pub")
            body = MultiPartFormDataContent(
                formData {
                    append(AUTHOR, user.login)
                    append(TITLE, title)
                    append(DESCRIPTION, description)
                    append(PDF_IDS, "")
                }
            )
        }
    }

    override suspend fun deletePublication(id: Long, user: User) {
        cache.getPubs().find { it.id == id }?.let {
            client.delete<Unit> {
                header(auth(), token(user.login))
                url(it.delete.href)
                cache.removePdf(it.id)
            }
        }
    }

    override suspend fun unlinkPdf(name: String, pubId: Long, user: User) {
        val pdfs = cache.getPdfs()
        val publications = cache.getPubs()
        publications.find { publicationResponse -> publicationResponse.id == pubId }?.run {
            val pdf = pdfs.find { pdf -> pdf.fileName == name }
            pdf?.id?.let { pdfId ->
                client.post<Unit> {
                    header(auth(), token(user.login))
                    url("${unmount.href}$pdfId")
                    contentType(ContentType.Application.Json)
                }
            }
        }
    }

    override suspend fun linkPdf(name: String, pubId: Long, user: User) {
        cache.getPubs().find { pub -> pub.id == pubId }?.run {
            val pdf = cache.getPdfs().find { pdf -> pdf.fileName == name }
            pdf?.id?.let { pdfId ->
                client.postWithToken("${mount.href}$pdfId", user)
            }
        }
    }

    override suspend fun getAllPublications(user: User): List<PublicationResponse> =
        (client.getPublications(user)
            ?.apply {
                cache.putPubs(this)
            } ?: emptyList())
}