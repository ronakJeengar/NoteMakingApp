package com.ronakj.noting

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel(){

    @OptIn(DelicateCoroutinesApi::class)
    fun insert(note : Note) = GlobalScope.launch {
        repository.insert(note)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun delete(note : Note) = GlobalScope.launch {
        repository.delete(note)
    }

    fun getAllNotes() = repository.getAllNote()

}