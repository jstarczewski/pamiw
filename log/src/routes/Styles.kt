package com.jstarczewski.log.routes

import com.jstarczewski.log.MainCss
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.styles() {

    get<MainCss> {
        call.resolveResource("/blog.css")?.let {
            call.respond(it)
        }
    }
}
