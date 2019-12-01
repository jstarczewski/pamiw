package com.jstarczewski.updf.util

import com.jstarczewski.updf.db.PdfDataSource
import com.jstarczewski.updf.db.base.PdfDatabase
import io.ktor.config.ApplicationConfig
import io.ktor.util.KtorExperimentalAPI
import java.io.File
import java.io.IOException

object Injection {

    @KtorExperimentalAPI
    fun provideUploadDir(config: ApplicationConfig): File {
        val uploadDirPath: String = config.property("upload.dir").getString()
        val uploadDir = File(uploadDirPath)
        if (!uploadDir.mkdirs() && !uploadDir.exists()) {
            throw IOException("Failed to create directory ${uploadDir.absolutePath}")
        }
        return uploadDir
    }

    fun provideLocalDataSource(uploadDir: File): PdfDataSource = PdfDatabase(uploadDir)
}