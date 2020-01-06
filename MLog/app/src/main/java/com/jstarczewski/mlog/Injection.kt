package com.jstarczewski.mlog

import com.jstarczewski.mlog.model.User
import com.jstarczewski.mlog.remote.cache.ResponseCache
import com.jstarczewski.mlog.remote.cache.ResponseDataSource
import com.jstarczewski.mlog.remote.pdf.PdfDataSource
import com.jstarczewski.mlog.remote.pdf.RemotePdfDataSource
import com.jstarczewski.mlog.remote.pub.PublicationDataSource
import com.jstarczewski.mlog.remote.pub.RemotePublicationDataSource
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature

object Injection {

    private val client = HttpClient {
        install(JsonFeature)
    }

    private fun provideHttpClient() = client

    private fun provideResponseCache(): ResponseCache = ResponseDataSource.getInstance()

    fun provideUser(): User = User()

    fun providePdfDataSource(): PdfDataSource =
        RemotePdfDataSource.getInstance(provideHttpClient(), provideResponseCache())

    fun providePublicationDataSource(): PublicationDataSource =
        RemotePublicationDataSource.getInstance(
            provideHttpClient(), provideResponseCache()
        )
}
