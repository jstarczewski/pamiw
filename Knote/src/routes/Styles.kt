package com.jstarczewski.knote.routes

import com.jstarczewski.knote.MainCss
import io.ktor.application.call
import io.ktor.http.content.resolveResource
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route


fun Route.styles() {

    get<MainCss> {
        call.resolveResource("/blog.css")?.let {
            call.respond(it)
        }
    }
}