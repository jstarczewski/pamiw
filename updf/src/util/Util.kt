package com.jstarczewski.updf.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import java.io.InputStream
import java.io.OutputStream

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


private const val extensionType = "idx"

private val digitsOnlyRegex = "\\d+".toRegex()

fun File.getAllIds(extension: String = extensionType) =
    this.listFiles { f -> f.extension == extension && f.nameWithoutExtension.matches(digitsOnlyRegex) }
        .mapTo(ArrayList()) { it.nameWithoutExtension.toLong() }

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
