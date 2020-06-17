package com.jstarczewski.knote.routes

import com.jstarczewski.knote.KnoteSession
import com.jstarczewski.knote.Login
import com.jstarczewski.knote.UserPage
import com.jstarczewski.knote.credentials.Validator
import com.jstarczewski.knote.db.user.UserDataSource
import com.jstarczewski.knote.util.redirect
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.Parameters
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

private const val GENERIC_CREDENTIALS_ERROR = "Invalid credentials"

fun Routing.login(
    db: UserDataSource,
    checkPassword: (String, ByteArray, String) -> Boolean,
    loginValidators: List<Validator>,
    delayGenerator: () -> Long
) {

    get<Login> {
        with(call) {
            sessions.get<KnoteSession>()?.let {
                db.userById(it.userId)
            }?.run {
                redirect(UserPage())
            } ?: run {
                respond(FreeMarkerContent("login.ftl", mapOf("userId" to it.userId, "error" to it.error), ""))
            }
        }
    }

    post<Login> {
        val post = call.receive<Parameters>()
        val login = post["login"]
        val password = post["password"]
        login?.let { login ->
            val error = Login(error = login)
            var containsLoginErrors = false
            loginValidators.forEach {
                if (it.validate(login) == null) {
                    call.redirect(error.copy(error = it.getValidationErrorMessage()))
                    containsLoginErrors = true
                }
            }
            if (!containsLoginErrors) {
                password?.run {
                    val error = Login(login)
                    db.userByLogin(login)?.let { user ->
                        if (checkPassword(password, user.salt, user.password))
                            runBlocking {
                                delay(delayGenerator())
                                call.sessions.set(KnoteSession(user.userId))
                                call.redirect(UserPage())
                            }
                        else {
                            call.redirect(error.copy(error = GENERIC_CREDENTIALS_ERROR))
                        }
                    } ?: run {
                        call.redirect(error.copy(error = GENERIC_CREDENTIALS_ERROR))
                    }

                } ?: run {
                    val error = Login(login)
                    call.redirect(error.copy(error = GENERIC_CREDENTIALS_ERROR))
                }
            }
        } ?: run {
            val error = Login("null")
            call.redirect(error.copy(error = GENERIC_CREDENTIALS_ERROR))
        }
    }
}