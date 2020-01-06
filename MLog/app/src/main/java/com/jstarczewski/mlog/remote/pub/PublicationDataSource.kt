package com.jstarczewski.mlog.remote.pub

import com.jstarczewski.mlog.model.User
import com.jstarczewski.mlog.model.publication.api.PublicationResponse

interface PublicationDataSource {

    suspend fun addPublication(title: String, description: String, user: User)

    suspend fun deletePublication(id: Long, user: User)

    suspend fun unlinkPdf(name: String, pubId: Long, user: User)

    suspend fun linkPdf(name: String, pubId: Long, user: User)

    suspend fun getAllPublications(user: User): List<PublicationResponse>
}