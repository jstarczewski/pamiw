package com.jstarczewski.updf

import com.jstarczewski.updf.auth.JwtConfig
import com.jstarczewski.updf.auth.User
import com.jstarczewski.updf.util.Injection
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.PartialContent
import io.ktor.jackson.jackson
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Location("/upload")
class Upload

@Location("/pdf/{id}")
data class Pdf(val id: Long)

@Location("/pdf/all/{username}")
data class UserPdf(val username: String)

const val CONFIG_PATH = "updf"
const val REALM = "WEBOWECZKA"

@KtorExperimentalLocationsAPI
@KtorExperimentalAPI
@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    val uploadDir = Injection.provideUploadDir(environment.config.config(CONFIG_PATH))
    val database = Injection.provideLocalDataSource(uploadDir)

    install(ContentNegotiation) {
        jackson {

        }
    }
    install(DefaultHeaders)
    install(Locations)
    install(PartialContent)
    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            realm = REALM
            validate {
                it.payload.getClaim(JwtConfig.claimName)
                    .asString()
                    ?.let { claim ->
                        User(claim)
                    }
            }
        }
    }

    routing {
        upload(database, uploadDir)
        pdf(database)

        get("/demo") {
            call.respondText("HELLO WORLD!")
        }
    }
}

