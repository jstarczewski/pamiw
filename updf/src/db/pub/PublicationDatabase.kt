package com.jstarczewski.updf.db.pub

import com.jstarczewski.updf.db.Publication
import com.jstarczewski.updf.util.tryOrNull
import java.io.File

class PublicationDatabase(private val uploadDir: File) : CachedPublicationDatabase(uploadDir), PublicationDataSource {

    private fun publicationById(id: Long) =
        cache.get(id) ?: tryOrNull {
            gson.fromJson(File(uploadDir, "$id.idx").readText(), Publication::class.java)
                ?.also {
                    cache.put(id, it)
                }
        }

    override fun savePublication(author: String, title: String, description: String, pdfIds: List<Long>?): Long {
        val id = nextId()
        val pub = Publication(id, author, title, description, pdfIds)
        File(uploadDir, "$id.idx").writeText(gson.toJson(pub))
        allIds.add(id)
        cache.put(id, pub)
        return id
    }

    override fun getPublicationById(id: Long) =
        cache.get(id) ?: tryOrNull {
            gson.fromJson(File(uploadDir, "$id.idx").readText(), Publication::class.java)
                ?.also {
                    cache.put(id, it)
                }
        }

    override fun deletePublication(id: Long): Boolean {
        val pub = publicationById(id)
        pub?.let {
            cache.remove(id)
            File(uploadDir, "$id.idx").delete()
            return true
        }
        return false
    }

    override fun getAllPublications() = allIds
        .asSequence()
        .mapNotNull { publicationById(it) }

    override fun linkPdfWithPublication(pdfId: Long, publicationId: Long): Boolean {
        publicationById(publicationId)?.let {
            val newIds = it.pdfIds?.toMutableList()
                ?.apply {
                    takeIf { list -> list.contains(pdfId).not() }?.add(pdfId)
                } ?: listOf(pdfId)
            val pub = Publication(it.id, it.author, it.title, it.description, newIds)
            cache.replace(publicationId, pub)
            File(uploadDir, "$publicationId.idx").writeText(gson.toJson(pub))
            return true
        }
        return false
    }

    override fun unlinkPdfWithPublication(pdfId: Long, publicationId: Long): Boolean {
        publicationById(publicationId)?.let {
            val newIds = it.pdfIds?.toMutableList()?.apply {
                takeIf { list -> list.contains(pdfId) }?.remove(pdfId)
            }
            val pub = Publication(it.id, it.author, it.title, it.description, newIds)
            cache.replace(publicationId, pub)
            File(uploadDir, "$publicationId.idx").writeText(gson.toJson(pub))
            return true
        }
        return false
    }
}