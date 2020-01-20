package com.jstarczewski.log.routes

import com.jstarczewski.log.db.UserDataSource
import com.jstarczewski.log.Index
import com.jstarczewski.log.LogSession
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.sessions.get
import io.ktor.sessions.sessions

fun Routing.index(db: UserDataSource) {

    get<Index> {
        val user = call.sessions.get<LogSession>()?.let {
            db.userById(it.userId.toLong())
        }
        call.respond(FreeMarkerContent("index.ftl", "user" to user))
    }
}