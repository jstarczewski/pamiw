package com.jstarczewski.log.util

import com.jstarczewski.log.db.UserDatabase
import com.jstarczewski.log.cache.ResponseDataSource
import io.ktor.config.ApplicationConfig
import io.ktor.util.KtorExperimentalAPI
import java.io.File
import java.io.IOException

object Injection {

    @KtorExperimentalAPI
    fun provideUploadDir(config: ApplicationConfig, property: String): File {
        val uploadDirPath: String = config.property(property).getString()
        val uploadDir = File(uploadDirPath)
        if (!uploadDir.mkdirs() && !uploadDir.exists()) {
            throw IOException("Failed to create directory ${uploadDir.absolutePath}")
        }
        return uploadDir
    }

    fun provideUserDataSource(uploadDir: File) = UserDatabase(uploadDir)

    fun provideResponseDataSource() = ResponseDataSource()
}