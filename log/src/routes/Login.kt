package com.jstarczewski.log.routes

import com.jstarczewski.log.db.UserDataSource
import com.jstarczewski.log.Index
import com.jstarczewski.log.LogSession
import com.jstarczewski.log.Logout
import com.jstarczewski.log.util.redirect
import io.ktor.application.call
import io.ktor.locations.get
import io.ktor.routing.Routing
import io.ktor.sessions.clear
import io.ktor.sessions.sessions

fun Routing.login(db: UserDataSource) {

    /*
    get<Login> {
        with(call) {
            sessions.get<LogSession>()?.let {
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
            password?.run {
                val error = Login(login)
                db.user(login, this)?.let { user ->
                    call.sessions.set(LogSession(user.userId))
                    call.redirect(UserUpload())
                } ?: run {
                    call.redirect(error.copy(error = "Invalid username or password"))
                }
            } ?: run {
                val error = Login(login)
                call.redirect(error.copy(error = "Unexpected Error appeared"))
            }
        } ?: run {
            val error = Login("null")
            call.redirect(error.copy(error = "Invalid username or password"))
        }

    }*/

    get<Logout> {
        call.sessions.clear<LogSession>()
        call.redirect(Index())
    }
}