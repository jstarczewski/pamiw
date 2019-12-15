package com.jstarczewski.updf.db.pub

import com.jstarczewski.updf.db.Publication

interface PublicationDataSource {

    fun savePublication(author: String, title: String, description: String, pdfIds: List<Long>?): Long

    fun deletePublication(id: Long): Boolean

    fun getPublicationById(id: Long) : Publication?

    fun getAllPublications(): Sequence<Publication>

    fun linkPdfWithPublication(pdfId: Long, publicationId: Long): Boolean

    fun unlinkPdfWithPublication(pdfId: Long, publicationId: Long): Boolean
}