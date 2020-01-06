package com.jstarczewski.mlog.remote.pdf

import com.jstarczewski.log.publication.api.PdfResponse
import com.jstarczewski.mlog.model.User

interface PdfDataSource {

    suspend fun deletePdf(id: Long, user: User)

    suspend fun getAllPdfs(user: User): List<PdfResponse>?
}