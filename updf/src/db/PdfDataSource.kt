package com.jstarczewski.updf.db

import java.io.File

interface PdfDataSource {

    fun getPdfById(id: Long) : Pdf?

    fun savePdf(userName: String, title: String, file: File): Long

    fun getAllPdfFiles(): Sequence<Pdf>
}