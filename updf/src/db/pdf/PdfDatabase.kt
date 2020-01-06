package com.jstarczewski.updf.db.pdf

import com.jstarczewski.updf.db.Pdf
import com.jstarczewski.updf.util.tryOrNull
import java.io.File


class PdfDatabase(private val uploadDir: File) : CachePdfDatabase(uploadDir),
    PdfDataSource {

    override fun getPdfById(id: Long) = pdfById(id)

    override fun savePdf(userName: String, title: String): Long {
        val id = nextId()
        val pdf = Pdf(id, title, userName)
        File(uploadDir, "$id.idx").writeText(gson.toJson(pdf))
        allIds.add(id)
        cache.put(id, pdf)
        return id
    }

    override fun deletePdf(id: Long): Boolean {
        val pdf = pdfById(id)
        pdf?.let {
            val file = File(uploadDir, "${pdf.fileName}.pdf")
            file.delete()
            cache.remove(id)
            File(uploadDir, "$id.idx").delete()
            return true
        }
        return false
    }

    override fun getAllPdfFiles(): Sequence<Pdf> = allIds.asSequence().mapNotNull { pdfById(it) }

    private fun pdfById(id: Long) =
        cache.get(id) ?: tryOrNull {
            gson.fromJson(File(uploadDir, "$id.idx").readText(), Pdf::class.java)
                ?.also {
                    cache.put(id, it)
                }
        }
}