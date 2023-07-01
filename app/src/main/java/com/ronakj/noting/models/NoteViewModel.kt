package com.ronakj.noting.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ronakj.noting.db.NoteDatabase
import com.ronakj.noting.db.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val noteRepository: NoteRepository
    val allNotes: LiveData<List<Note>>

    init {
        val dao = NoteDatabase.getDatabase(application).getNoteDao()
        noteRepository = NoteRepository(dao)
        allNotes = noteRepository.allNotes

    }

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.insert(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.delete(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteRepository.update(note)
        }
    }

}