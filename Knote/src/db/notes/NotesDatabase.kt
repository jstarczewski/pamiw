package com.jstarczewski.knote.db.notes

import com.jstarczewski.knote.db.model.Note
import com.jstarczewski.knote.util.tryOrNull
import java.io.File

class NotesDatabase(private val uploadDir: File) : CacheNotesDatabase(uploadDir), NotesDataSource {

    companion object {

        private const val NOTE_JSON_FILE_APPENDIX = ".idx"
    }

    override fun getNoteById(id: Long): Note? = noteById(id)

    override fun saveNote(author: String, title: String, content: String, isPublic: Boolean): Long {
        val id = nextId()
        val note = Note(id, author, title, content, isPublic)
        File(uploadDir, id.toString() + NOTE_JSON_FILE_APPENDIX).writeText(gson.toJson(note))
        allIds.add(id)
        cache.put(id, note)
        return id
    }

    override fun deleteNote(id: Long): Boolean {
        noteById(id)?.let {
            cache.remove(id)
            File(uploadDir, id.toString() + NOTE_JSON_FILE_APPENDIX).delete()
            return true
        }
        return false
    }

    override fun getAllNotes(): Sequence<Note> =
        allIds.asSequence().mapNotNull { noteById(it) }

    private fun noteById(id: Long) =
        cache.get(id) ?: tryOrNull {
            gson.fromJson(File(uploadDir, "$id.idx").readText(), Note::class.java)
                ?.also {
                    cache.put(id, it)
                }
        }
}