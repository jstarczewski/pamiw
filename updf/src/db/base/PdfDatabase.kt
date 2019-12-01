package com.jstarczewski.updf.db.base

import com.jstarczewski.updf.db.Pdf
import com.jstarczewski.updf.db.PdfDataSource
import com.jstarczewski.updf.util.getAllIds
import com.jstarczewski.updf.util.tryOrNull
import java.io.File
import java.util.concurrent.atomic.AtomicLong


class PdfDatabase(private val uploadDir: File) : CachePdfDatabase(), PdfDataSource {

    private val allIds by lazy { uploadDir.getAllIds() }

    private val biggestId by lazy { AtomicLong(allIds.max() ?: 0) }

    private fun nextId() = biggestId.incrementAndGet()

    fun listAll(): Sequence<Pdf> = allIds.asSequence().mapNotNull { pdfById(it) }

    override fun getPdfById(id: Long) = pdfById(id)

    override fun savePdf(userName: String, title: String) = addPdf(userName, title)

    override fun getAllPdfFiles(): Sequence<Pdf> = listAll()

    private fun pdfById(id: Long) =
        pdfCache.get(id) ?: tryOrNull {
            gson.fromJson(File(uploadDir, "$id.idx").readText(), Pdf::class.java)
                ?.also {
                    pdfCache.put(id, it)
                }
        }

    private fun addPdf(userName: String, title: String): Long {
        val id = nextId()
        val pdf = Pdf(id, title, userName)
        File(uploadDir, "$id.idx").writeText(gson.toJson(pdf))
        allIds.add(id)
        pdfCache.put(id, pdf)
        return id
    }
}