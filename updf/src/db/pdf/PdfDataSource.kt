package com.jstarczewski.updf.db.pdf

import com.jstarczewski.updf.db.Pdf

interface PdfDataSource {

    fun getPdfById(id: Long): Pdf?

    fun savePdf(userName: String, title: String): Long

    fun deletePdf(id: Long): Boolean

    fun getAllPdfFiles(): Sequence<Pdf>
}