package com.jstarczewski.knote

import com.jstarczewski.knote.credentials.validators.PlainTextAndNumbersValidator
import com.jstarczewski.knote.credentials.validators.WhitespaceValidator
import com.jstarczewski.knote.credentials.validators.CapitalLetterValidator
import com.jstarczewski.knote.credentials.validators.EmptyValidator
import com.jstarczewski.knote.password.validators.LengthValidator
import com.jstarczewski.knote.password.validators.LetterValidator
import com.jstarczewski.knote.password.validators.NumberValidator
import com.jstarczewski.knote.routes.*
import com.jstarczewski.knote.util.HashGenerator
import com.jstarczewski.knote.util.Injection
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.XForwardedHeaderSupport
import io.ktor.freemarker.FreeMarker
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.Locations
import io.ktor.network.tls.certificates.generateCertificate
import io.ktor.routing.routing
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.hex
import java.io.File
import kotlin.random.Random


@Location("/")
class Index

@Location("/styles/main.css")
class MainCss

@Location("/user")
class UserPage(val error: String = "")

@Location("/user/note")
class AddNote(val error: String = "")

@Location("/user/note/delete/{id}")
class DeleteNote(val id: Long)

@Location("/login")
data class Login(val userId: String = "", val error: String = "")

@Location("/register")
data class Register(val login: String = "", val error: String = "")

@Location("/user/password")
data class ChangePassword(val error: String = "")

@Location("/logout")
class Logout

data class KnoteSession(val userId: Long)

private const val UPLOAD_DIR_CONFIG_PATH = "Knote"
private const val SESSION_NAME = "SESSION_LOG"
private const val BASE_PACKAGE_PATH = "templates"

private const val USERS_DIR = "user.dir"
private const val NOTES_DIR = "note.dir"
private const val SESSION_KEY = "sessionKey"


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    val notesUploadDir = Injection.provideUploadDir(environment.config.config(UPLOAD_DIR_CONFIG_PATH), NOTES_DIR)
    val usersUploadDir = Injection.provideUploadDir(environment.config.config(UPLOAD_DIR_CONFIG_PATH), USERS_DIR)
    val userDb = Injection.provideUserDataSource(usersUploadDir)
    val notesDb = Injection.provideNotesDataSource(notesUploadDir)

    val hashKey = hex(environment.config.config(UPLOAD_DIR_CONFIG_PATH).property(SESSION_KEY).getString())

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, BASE_PACKAGE_PATH)
    }
    install(Locations)
    install(XForwardedHeaderSupport)
    install(Sessions) {
        cookie<KnoteSession>(
            SESSION_NAME
        ) {
            transform(SessionTransportTransformerMessageAuthentication(hashKey))
        }
    }
    val hashGenerator = HashGenerator()
    val hashFunction = { s: String -> hashGenerator.getHashedPassword(s) }
    val checkPassword =
        { password: String, salt: ByteArray, hashedPassword: String ->
            hashGenerator.checkPasswords(password, salt, hashedPassword)
        }
    val random = Random(hashKey.hashCode())

    val passwordValidators =
        listOf(
            EmptyValidator(),
            LengthValidator(),
            NumberValidator(),
            LetterValidator(),
            WhitespaceValidator(),
            CapitalLetterValidator()
        )

    val loginValidators = listOf(
        EmptyValidator(),
        PlainTextAndNumbersValidator()
    )

    routing {
        styles()
        index(notesDb)
        login(
            userDb,
            checkPassword,
            loginValidators,
            { random.nextLong(0, 3000) }
        )
        logout()
        userPage(userDb, notesDb)
        addNote(userDb, notesDb)
        deleteNote(userDb, notesDb)
        register(
            userDb,
            passwordValidators,
            loginValidators,
            hashFunction
        )
        changePassword(
            userDb, listOf(
                LengthValidator(),
                NumberValidator(),
                LetterValidator(),
                WhitespaceValidator(),
                CapitalLetterValidator()
            ),
            checkPassword,
            hashFunction
        )
    }
}

object CertificateGenerator {

    @JvmStatic
    fun main(args: Array<String>) {
        val jksFile = File("build/temporary.jks").apply {
            parentFile.mkdirs()
        }

        if (!jksFile.exists()) {
            generateCertificate(jksFile) // Generates the certificate
        }
        io.ktor.server.netty.EngineMain.main(args)
    }
}
