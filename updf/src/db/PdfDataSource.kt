package com.jstarczewski.updf.db

interface PdfDataSource {

    fun getPdfById(id: Long): Pdf?

    fun savePdf(userName: String, title: String): Long

    fun getAllPdfFiles(): Sequence<Pdf>
}