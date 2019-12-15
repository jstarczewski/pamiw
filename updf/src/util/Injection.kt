package com.jstarczewski.updf.util

import com.jstarczewski.updf.db.pdf.PdfDataSource
import com.jstarczewski.updf.db.pdf.PdfDatabase
import com.jstarczewski.updf.db.pub.PublicationDataSource
import com.jstarczewski.updf.db.pub.PublicationDatabase
import io.ktor.config.ApplicationConfig
import io.ktor.util.KtorExperimentalAPI
import java.io.File
import java.io.IOException

object Injection {

    @KtorExperimentalAPI
    fun providePdfUploadDir(config: ApplicationConfig): File {
        val uploadDirPath: String = config.property("upload.dir").getString()
        val uploadDir = File(uploadDirPath)
        if (!uploadDir.mkdirs() && !uploadDir.exists()) {
            throw IOException("Failed to create directory ${uploadDir.absolutePath}")
        }
        return uploadDir
    }

    @KtorExperimentalAPI
    fun providePublicationUploadDir(config: ApplicationConfig): File {
        val uploadDirPath: String = config.property("publication.dir").getString()
        val uploadDir = File(uploadDirPath)
        if (!uploadDir.mkdirs() && !uploadDir.exists()) {
            throw IOException("Failed to create directory ${uploadDir.absolutePath}")
        }
        return uploadDir
    }

    fun providePdfLocalDataSource(uploadDir: File): PdfDataSource =
        PdfDatabase(uploadDir)

    fun providePublicationsLocalDataSource(uploadDir: File): PublicationDataSource =
        PublicationDatabase(uploadDir)
}