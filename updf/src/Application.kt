package com.jstarczewski.updf

import com.jstarczewski.updf.db.PdfDataSource
import com.jstarczewski.updf.db.PdfDatabase
import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.response.*
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.vertx.core.Vertx
import io.vertx.redis.client.Redis
import io.vertx.redis.client.RedisAPI
import io.vertx.redis.client.RedisOptions
import java.io.File
import java.io.IOException

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Location("/upload")
class Upload

@Suppress("unused") // Referenced in application.conf
fun Application.module() {
    val config = environment.config.config("updf")
    val uploadDirPath: String = config.property("upload.dir").getString()
    val uploadDir = File(uploadDirPath)
    if (!uploadDir.mkdirs() && !uploadDir.exists()) {
        throw IOException("Failed to create directory ${uploadDir.absolutePath}")
    }
    val database = PdfDatabase(uploadDir) as PdfDataSource

    val server = embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            jackson {

            }
        }
        install(DefaultHeaders)
        install(Locations)
        routing {
            upload(database, uploadDir)
            get("/") {
                call.respondText("Hello World!", ContentType.Text.Plain)
            }
            get("/demo") {
                call.respondText("HELLO WORLD!")
            }
            get("/snippets") {
                call.respond(mapOf("snippet" to true))
            }
        }

    }
    server.start(wait = true)
}

