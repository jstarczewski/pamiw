package com.jstarczewski.log.util

import com.jstarczewski.log.auth.JwtConfig
import com.jstarczewski.log.publication.api.Action
import io.ktor.application.ApplicationCall
import io.ktor.http.Headers
import io.ktor.locations.locations
import io.ktor.request.host
import io.ktor.request.port
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import java.io.InputStream
import java.io.OutputStream

suspend fun InputStream.copyToSuspend(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    yieldSize: Int = 4 * 1024 * 1024,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Long {
    return withContext(dispatcher) {
        val buffer = ByteArray(bufferSize)
        var bytesCopied = 0L
        var bytesAfterYield = 0L
        while (true) {
            val bytes = read(buffer).takeIf { it >= 0 } ?: break
            out.write(buffer, 0, bytes)
            if (bytesAfterYield >= yieldSize) {
                yield()
                bytesAfterYield %= yieldSize
            }
            bytesCopied += bytes
            bytesAfterYield += bytes
        }
        return@withContext bytesCopied
    }
}

fun <T> tryOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (exception: Exception) {
        null
    }
}

const val AUTHORIZATION = "Authorization"

fun auth() = AUTHORIZATION

fun token(login: String) = "Bearer ${JwtConfig.makeToken(login)}"

private const val extensionType = "idx"

private val digitsOnlyRegex = "\\d+".toRegex()

fun File.getAllIds(extension: String = extensionType) =
    this.listFiles { f -> f.extension == extension && f.nameWithoutExtension.matches(digitsOnlyRegex) }
        .mapTo(ArrayList()) { it.nameWithoutExtension.toLong() }

const val def =
    "http://updf:8080"

fun Action.applyBaseUrl() = apply {
    href = def + this.href
}

fun <T : Any> T.assign(block: T.() -> Unit) {
    this.block()
}

operator fun Headers.plus(other: Headers): Headers = when {
    this.isEmpty() -> other
    other.isEmpty() -> this
    else -> Headers.build {
        appendAll(this@plus)
        appendAll(other)
    }
}


suspend fun ApplicationCall.redirect(location: Any) {
    val host = request.host() ?: "localhost"
    val portSpec = request.port().let { if (it == 80) "" else ":$it" }
    val address = host + portSpec
    respondRedirect("http://$address${application.locations.href(location)}")
}
