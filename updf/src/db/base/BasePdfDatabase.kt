package com.jstarczewski.updf.db.base

import com.jstarczewski.updf.db.Pdf
import com.jstarczewski.updf.getAllIds
import com.jstarczewski.updf.tryOrNull
import java.io.File
import java.util.concurrent.atomic.AtomicLong


abstract class BasePdfDatabase(private val uploadDir: File) : CachePdfDatabase(), IdHelper {

    override val allIds by lazy { uploadDir.getAllIds() }

    override val biggestId by lazy { AtomicLong(allIds.max() ?: 0) }

    override fun nextId() = biggestId.incrementAndGet()

    fun listAll(): Sequence<Pdf> = allIds.asSequence().mapNotNull { pdfById(it) }

    protected fun pdfById(id: Long) =
        pdfCache.get(id)?.let {
            tryOrNull {
                gson.fromJson(File(uploadDir, "$id.idx").readText(), Pdf::class.java)
                    ?.also {
                        pdfCache.put(id, it)
                    }
            }
        }

    protected fun addPdf(userName: String, title: String, file: File): Long {
        val id = nextId()
        val pdf = Pdf(id, title, userName)
        File(uploadDir, "$id.idx").writeText(gson.toJson(pdf))
        allIds.add(id)
        pdfCache.put(id, pdf)
        return id
    }
}