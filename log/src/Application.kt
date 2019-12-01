package com.jstarczewski.log

import com.jstarczewski.log.routes.*
import com.jstarczewski.log.util.Injection
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.features.ConditionalHeaders
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.freemarker.FreeMarker
import io.ktor.jackson.jackson
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.routing.routing
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.util.InternalAPI
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.hex

@Location("/login")
data class Login(val userId: String = "", val error: String = "")

@Location("/")
class Index()

@Location("/logout")
class Logout()

@Location("/user")
class UserPage

@Location("user/{id}")
data class UserPdf(val id: Long)

@Location("/hello")
class Hello

@Location("/user/upload")
class UserUpload

@Location("/styles/main.css")
class MainCss

data class LogSession(val userId: String)

const val defaultPath = "http://updf:8080/"

@KtorExperimentalAPI
private val hashKey = hex("6819b57a326945c1968f45236589")

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private const val UPLOAD_DIR_CONFIG_PATH = "log"
private const val SESSION_NAME = "SESSION_LOG"
private const val BASE_PACKAGE_PATH = "templates"

@KtorExperimentalLocationsAPI
@KtorExperimentalAPI
@InternalAPI
@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    val client = HttpClient {
        install(JsonFeature)
    }
    val uploadDir = Injection.provideUploadDir(environment.config.config(UPLOAD_DIR_CONFIG_PATH))
    val datasource = Injection.provideLocalDataSource()

    install(ConditionalHeaders)
    install(ContentNegotiation) {
        jackson {}
    }
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, BASE_PACKAGE_PATH)
    }
    install(DefaultHeaders)
    install(Locations)
    install(Sessions) {
        cookie<LogSession>(
            SESSION_NAME
        ) {
            transform(SessionTransportTransformerMessageAuthentication(hashKey))
        }
    }
    routing {
        styles()
        index(datasource)
        login(datasource)
        userPage(datasource, client)
        userUpload(datasource, client, uploadDir)
    }
}


