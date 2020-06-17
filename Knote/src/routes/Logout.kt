package com.jstarczewski.knote.routes

import com.jstarczewski.knote.Index
import com.jstarczewski.knote.KnoteSession
import com.jstarczewski.knote.Logout
import com.jstarczewski.knote.util.redirect
import io.ktor.application.call
import io.ktor.locations.get
import io.ktor.routing.Routing
import io.ktor.sessions.clear
import io.ktor.sessions.sessions


fun Routing.logout() {

    get<Logout> {
        call.sessions.clear<KnoteSession>()
        call.redirect(Index())
    }
}