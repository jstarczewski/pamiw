package com.jstarczewski.knote.util

import com.jstarczewski.knote.db.notes.NotesDatabase
import com.jstarczewski.knote.db.user.UserDatabase
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

    fun provideNotesDataSource(uploadDir: File) = NotesDatabase(uploadDir)
}