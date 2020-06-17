package com.jstarczewski.knote.db.notes

import com.jstarczewski.knote.db.model.Note

interface NotesDataSource {

    fun getNoteById(id: Long): Note?

    fun saveNote(author: String, title: String, content: String, isPublic: Boolean): Long

    fun deleteNote(id: Long): Boolean

    fun getAllNotes(): Sequence<Note>
}