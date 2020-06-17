package com.jstarczewski.knote.routes

import com.jstarczewski.knote.Index
import com.jstarczewski.knote.db.notes.NotesDataSource
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Routing


fun Routing.index(notesDb: NotesDataSource) {

    get<Index> {
        val notes = notesDb.getAllNotes().filter { it.isPublic }.toList()
        call.respond(FreeMarkerContent("index.ftl", mapOf("all" to notes)))
    }
}