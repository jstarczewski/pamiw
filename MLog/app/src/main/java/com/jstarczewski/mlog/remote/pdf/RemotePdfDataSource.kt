package com.jstarczewski.mlog.remote.pdf

import com.jstarczewski.log.publication.api.PdfResponse
import com.jstarczewski.mlog.auth
import com.jstarczewski.mlog.model.User
import com.jstarczewski.mlog.remote.cache.ResponseCache
import com.jstarczewski.mlog.remote.helpers.getPdfs
import com.jstarczewski.mlog.token
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.header
import io.ktor.client.request.url

class RemotePdfDataSource private constructor(
    private val client: HttpClient,
    private val responseCacheCreator: ResponseCache
) : PdfDataSource {

    companion object {

        private var INSTANCE: PdfDataSource? = null

        fun getInstance(client: HttpClient, responseCacheCreator: ResponseCache) =
            INSTANCE ?: RemotePdfDataSource(client, responseCacheCreator).also {
                INSTANCE = it
            }
    }

    override suspend fun deletePdf(id: Long, user: User) {
        responseCacheCreator.getPdfs().find { it.id == id }?.let {
            client.delete<Unit> {
                header(auth(), token(user.login))
                url(it.pdfAction.delete.href)
                responseCacheCreator.removePdf(it.id)
            }
        }
    }

    override suspend fun getAllPdfs(user: User): List<PdfResponse>? =
        client.getPdfs(user)?.apply {
            responseCacheCreator.putPdfs(this)
        } ?: emptyList()
}