package com.jstarczewski.log.helpers

import com.jstarczewski.log.db.UserDataSource
import com.jstarczewski.log.LogSession
import com.jstarczewski.log.db.User
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.util.pipeline.PipelineContext

suspend fun PipelineContext<Unit, ApplicationCall>.withSession(db: UserDataSource, block: suspend (User) -> Unit) =
    call.sessions.get<LogSession>()?.let { db.userById(it.userId.toLong()) }?.let {
        block(it)
    }
