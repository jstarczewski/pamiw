package com.jstarczewski.knote.util

import com.jstarczewski.knote.KnoteSession
import com.jstarczewski.knote.db.model.User
import com.jstarczewski.knote.db.user.UserDataSource
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.locations.locations
import io.ktor.request.host
import io.ktor.request.port
import io.ktor.response.respondRedirect
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.util.pipeline.PipelineContext
import java.io.File


fun <T> tryOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (exception: Exception) {
        null
    }
}

fun runWhenElementIsNull(block: () -> Unit, vararg parameters: String?) {
    if (parameters.size != parameters.filterNotNull().size)
        block()
}

fun Any.asHttpParameter() = "/$this"

fun String.toLongList() = tryOrNull {
    split(',').toLongList()
}

fun List<String>.toLongList() =
    tryOrNull {
        this.map {
            it.toLong()
        }
    }

fun List<*>.secondOrNull() = tryOrNull { this[1] }

fun List<*>.firstOrNull() = tryOrNull { this[0] }

fun validateEquality(password: String, repeatedPassword: String?) =
    if (password == repeatedPassword) password else null

private const val extensionType = "idx"

private val digitsOnlyRegex = "\\d+".toRegex()

fun File.getAllIds(extension: String = extensionType) =
    this.listFiles { f -> f.extension == extension && f.nameWithoutExtension.matches(digitsOnlyRegex) }
        .mapTo(ArrayList()) { it.nameWithoutExtension.toLong() }

suspend fun ApplicationCall.redirect(location: Any) {
    val host = request.host() ?: "localhost"
    val portSpec = request.port().let { if (it == 5000) "" else ":$it" }
    val address = host + portSpec

    respondRedirect("https://$address${application.locations.href(location)}")
}

suspend fun PipelineContext<Unit, ApplicationCall>.withSession(db: UserDataSource, block: suspend (User) -> Unit) =
    call.sessions.get<KnoteSession>()?.let { db.userById(it.userId.toLong()) }?.let {
        block(it)
    }