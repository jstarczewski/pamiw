package com.jstarczewski.updf.db

import com.jstarczewski.updf.db.base.BasePdfDatabase
import java.io.File

class PdfDatabase(uploadDir: File) : BasePdfDatabase(uploadDir), PdfDataSource {

    override fun getPdfById(id: Long) = pdfById(id)

    override fun savePdf(userName: String, title: String, file: File) = addPdf(userName, title, file)

    override fun getAllPdfFiles(): Sequence<Pdf> = listAll()
}