package com.jstarczewski.log

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jstarczewski.log.routes.*
import com.jstarczewski.log.util.Counter
import com.jstarczewski.log.util.Injection
import com.jstarczewski.log.util.Notifier
import com.jstarczewski.log.util.redirect
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.features.ConditionalHeaders
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.origin
import io.ktor.freemarker.FreeMarker
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.request.host
import io.ktor.request.port
import io.ktor.response.cacheControl
import io.ktor.response.respondTextWriter
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.sessions.*
import io.ktor.util.InternalAPI
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.hex
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay

@Location("/login")
data class Login(val userId: String = "", val error: String = "")

@Location("/")
class Index

@Location("/logout")
class Logout

@Location("/user")
class UserPage(val error: String = "")

@Location("/user/{id}")
data class UserPdf(val id: Long)

@Location("/user/upload")
class UserUpload(val error: String = "")

@Location("/styles/main.css")
class MainCss

@Location("/user/upload/pub")
class UserUploadPub(val error: String = "")

@Location("/user/pub/delete/{id}")
class UserPublicationDelete(val id: Long)

@Location("/user/pdf/delete/{id}")
class DeletePdf(val id: Long)

@Location("/user/pub/link/{id}")
class LinkPublication(val id: Long)

@Location("/user/pub/unlink/{id}")
class UnlinkPublication(val id: Long)

data class LogSession(val userId: String, val tag: String)

const val defaultPath = "http://updf:8080/"

@KtorExperimentalAPI
private val hashKey = hex("6819b57a326945c1968f45236589")

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private const val UPLOAD_DIR_CONFIG_PATH = "log"
private const val SESSION_NAME = "SESSION_LOG"
private const val BASE_PACKAGE_PATH = "templates"


private const val USERS_DIR = "user.dir"
private const val PDF_DIR = "upload.dir"

@ExperimentalCoroutinesApi
@KtorExperimentalLocationsAPI
@KtorExperimentalAPI
@InternalAPI
@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    val client = HttpClient {
        install(JsonFeature)
    }
    val uploadDir = Injection.provideUploadDir(environment.config.config(UPLOAD_DIR_CONFIG_PATH), PDF_DIR)
    val usersUploadDir = Injection.provideUploadDir(environment.config.config(UPLOAD_DIR_CONFIG_PATH), USERS_DIR)
    val datasource = Injection.provideUserDataSource(usersUploadDir)
    val responseDataSource = Injection.provideResponseDataSource()

    install(ConditionalHeaders)
    install(ContentNegotiation) {
        jackson {}
    }
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, BASE_PACKAGE_PATH)
    }
    install(Authentication) {
        oauth("google-oauth") {
            this.client = client
            providerLookup = { googleOauthProvider }
            urlProvider = {
                redirectUrl("/login")
            }
        }
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

    val notifier = Notifier()

    val counter = Counter()

    routing {
        styles()
        index(datasource)
        authenticate("google-oauth") {
            route("/login") {
                handle {
                    val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
                        ?: error("No principal")

                    val json = client.get<String>("https://www.googleapis.com/userinfo/v2/me") {
                        header("Authorization", "Bearer ${principal.accessToken}")
                    }

                    val data = ObjectMapper().readValue<Map<String, Any?>>(json)
                    val id = data["id"] as String?
                    val login = data["name"] as String?


                    if (id != null && login != null) {
                        datasource.userByLogin(login)?.let {
                            call.sessions.set(
                                LogSession(
                                    it.userId,
                                    counter.next().toString()
                                )
                            )

                            call.redirect(UserPage())
                        } ?: run {
                            datasource.saveUser(login, id)
                            datasource.userByLogin(login)?.let {
                                call.sessions.set(
                                    LogSession(
                                        it.userId,
                                        counter.next().toString()
                                    )
                                )
                                call.redirect(UserPage())
                            }
                        }
                    }
                }
            }
        }
        get("/sse") {
            try {
                call.sessions.get<LogSession>()?.let {
                    delay(1000)
                    notifier.getNotification(it)?.let {
                        call.respondSse(produce {
                            send(SseEvent(it))
                        })
                    }
                }
            } finally {

            }
        }
        login(datasource)
        userPage(datasource, client, responseDataSource, notifier)
        userUpload(datasource, client, uploadDir)
        userUploadPub(datasource, client, notifier)
        userPublicationDelete(datasource, client, responseDataSource)
        linkPublication(datasource, client, responseDataSource)
        unlinkPublication(datasource, client, responseDataSource)
        userPdfDelete(datasource, client, responseDataSource)
    }
}


val googleOauthProvider = OAuthServerSettings.OAuth2ServerSettings(
    name = "google",
    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
    accessTokenUrl = "https://www.googleapis.com/oauth2/v3/token",
    requestMethod = HttpMethod.Post,

    clientId = "501069263047-qodg9rvb8o1357aj3fjum94bmvg8bi3t.apps.googleusercontent.com",
    clientSecret = "MjMtCK1DZNuYnQ-C5HMlDFA_",
    defaultScopes = listOf("profile") // no email, but gives full name, picture, and id
)

private fun ApplicationCall.redirectUrl(path: String): String {
    val defaultPort = if (request.origin.scheme == "http") 80 else 443
    val hostPort = request.host()!! + request.port().let { port -> if (port == defaultPort) "" else ":$port" }
    val protocol = request.origin.scheme
    return "$protocol://$hostPort$path"
}

data class SseEvent(val data: String, val event: String? = null, val id: String? = null)

suspend fun ApplicationCall.respondSse(events: ReceiveChannel<SseEvent>) {
    response.cacheControl(CacheControl.NoCache(null))
    respondTextWriter(contentType = ContentType.Text.EventStream) {
        for (event in events) {
            if (event.id != null) {
                write("id: ${event.id}\n")
            }
            if (event.event != null) {
                write("event: ${event.event}\n")
            }
            for (dataLine in event.data.lines()) {
                write("data: $dataLine\n")
            }
            write("\n")
            flush()
        }
    }
}