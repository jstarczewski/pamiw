package com.jstarczewski.mlog.model.publication.api

import com.jstarczewski.log.publication.api.PdfResponse
import com.jstarczewski.mlog.applyBaseUrl
import java.io.Serializable

data class PublicationResponse(
    val id: Long,
    val author: String,
    val title: String,
    val description: String,
    val delete: Action,
    val mount: Action,
    val unmount: Action,
    val pdfs: List<PdfResponse>?
) : Serializable {

    companion object {

        fun toPublicationResponseWithBaseUrl(publicationResponse: PublicationResponse) =
            PublicationResponse(
                publicationResponse.id,
                publicationResponse.author,
                publicationResponse.title,
                publicationResponse.description,
                publicationResponse.delete.applyBaseUrl(),
                publicationResponse.mount.applyBaseUrl(),
                publicationResponse.unmount.applyBaseUrl(),
                publicationResponse.pdfs?.map { PdfResponse.toPdfResponseWithBaseUrl(it) }
            )
    }
}