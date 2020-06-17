package com.jstarczewski.knote.routes

import com.jstarczewski.knote.DeleteNote
import com.jstarczewski.knote.UserPage
import com.jstarczewski.knote.db.notes.NotesDataSource
import com.jstarczewski.knote.db.user.UserDataSource
import com.jstarczewski.knote.util.redirect
import com.jstarczewski.knote.util.withSession
import io.ktor.application.call
import io.ktor.locations.get
import io.ktor.routing.Routing

fun Routing.deleteNote(userDb: UserDataSource, notesDb: NotesDataSource) {

    get<DeleteNote> { params ->
        withSession(userDb) {
            notesDb.run {
                getAllNotes().find { it.id == params.id }?.let {
                    deleteNote(it.id)
                }
            }
        }
        call.redirect(UserPage())
    }
}