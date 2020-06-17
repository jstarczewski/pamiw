package com.jstarczewski.knote.routes

import com.jstarczewski.knote.KnoteSession
import com.jstarczewski.knote.Login
import com.jstarczewski.knote.Register
import com.jstarczewski.knote.UserPage
import com.jstarczewski.knote.credentials.Validator
import com.jstarczewski.knote.db.user.UserDataSource
import com.jstarczewski.knote.util.redirect
import com.jstarczewski.knote.util.validateEquality
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

private const val EQUALITY_ERROR = "Passwords must be equal"


fun Routing.register(
    userDb: UserDataSource,
    passwordValidators: List<Validator>,
    loginValidators: List<Validator>,
    hash: (String) -> Pair<String, ByteArray>
) {

    get<Register> {
        with(call) {
            sessions.get<KnoteSession>()?.let {
                userDb.userById(it.userId.toLong())
            }?.run {
                redirect(UserPage())
            } ?: run {
                respond(FreeMarkerContent("register.ftl", mapOf("login" to it.login, "error" to it.error), ""))
            }
        }
    }

    post<Register> {
        val post = call.receive<Parameters>()
        val login = post["login"]
        val password = post["password"]
        val repeatPassowrd = post["repeat_password"]
        login?.let { login ->
            val error = Register(login)
            var containsLoginErrors = false
            loginValidators.forEach {
                if (it.validate(login) == null) {
                    call.redirect(error.copy(error = it.getValidationErrorMessage(), login = login))
                    containsLoginErrors = true
                }
            }
            if (!containsLoginErrors) {
                password?.run {
                    var containsPasswordErrors = false
                    userDb.userByLogin(login)?.let { user ->
                        call.redirect(error.copy(error = "User already exists"))
                    } ?: run {
                        if (validateEquality(password, repeatPassowrd) == null) {
                            call.redirect(error.copy(error = EQUALITY_ERROR, login = login))
                        }
                        passwordValidators.forEach {
                            if (it.validate(password) == null) {
                                call.redirect(error.copy(error = it.getValidationErrorMessage(), login = login))
                                containsPasswordErrors = true
                            }
                        }
                        if (containsPasswordErrors.not()) {
                            val credentials = hash(password)
                            userDb.saveUser(login, credentials.first, credentials.second)
                            call.redirect(UserPage())
                        }
                    }
                } ?: run {
                    val error = Register(login)
                    call.redirect(error.copy(error = "Unexpected Error appeared"))
                }
            }
        } ?: run {
            val error = Login("null")
            call.redirect(error.copy(error = "Invalid username or credentials"))
        }
    }
}

