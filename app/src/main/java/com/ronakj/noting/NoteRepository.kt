package com.ronakj.noting

class NoteRepository(private val db: NoteDatabase) {

    suspend fun insert(note : Note) = db.getNoteDao().insert(note)

    suspend fun delete(note : Note) = db.getNoteDao().delete(note)

    suspend fun update(note: Note) = db.getNoteDao().update(note)

    fun getAllNote() = db.getNoteDao().getAllNotes()

}