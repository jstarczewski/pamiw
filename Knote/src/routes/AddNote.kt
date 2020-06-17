package com.jstarczewski.knote.routes

import com.jstarczewski.knote.AddNote
import com.jstarczewski.knote.UserPage
import com.jstarczewski.knote.db.notes.NotesDataSource
import com.jstarczewski.knote.db.user.UserDataSource
import com.jstarczewski.knote.util.redirect
import com.jstarczewski.knote.util.withSession
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing

private const val AUTHOR = "author"
private const val TITLE = "title"
private const val CONTENT = "content"
private const val PUBLIC = "public"

fun Routing.addNote(userDb: UserDataSource, notesDb: NotesDataSource) {

    post<AddNote> {
        withSession(userDb) { user ->
            val post = call.receive<Parameters>()
            val title = post[TITLE]
            val content = post[CONTENT]
            val public = post[PUBLIC] != null
            if (title?.isNotEmpty() == true &&
                content?.isNotEmpty() == true
            ) {
                notesDb.saveNote(user.login, title, content, public)
                call.redirect(UserPage())
            }
        }
    }

    get<AddNote> {
        withSession(userDb) { user ->
            call.respond(
                FreeMarkerContent(
                    "note.ftl", mapOf(
                        "userId" to user.userId,
                        "user" to user,
                        "error" to it.error
                    ), ""
                )
            )
        } ?: call.respond(HttpStatusCode.Forbidden)
    }
}