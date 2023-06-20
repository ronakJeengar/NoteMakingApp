package com.ronakj.noting

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ronakj.noting.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NoteAdapter.NoteItemClickInterface {

    private var binding: ActivityMainBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var fabAddNote: FloatingActionButton
    private lateinit var noteViewModel: NoteViewModel

    private lateinit var notes : List<Note>

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        recyclerView = binding?.recyclerView!!
        fabAddNote = binding?.addNote!!
        notes = ArrayList()

        noteAdapter = NoteAdapter(notes,this)
        val spanCount = 2
//        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = noteAdapter

        val noteRepository = NoteRepository(NoteDatabase(this))
        val factory = NoteViewModelFactory(noteRepository)

        noteViewModel = ViewModelProvider(this,factory)[NoteViewModel::class.java]

        noteViewModel.getAllNotes().observe(this){
            noteAdapter.notes = it
            noteAdapter.notifyDataSetChanged()
        }

        fabAddNote.setOnClickListener {
            showAddNoteDialog()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showAddNoteDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_box)
        val titleEditText = dialog.findViewById<EditText>(R.id.editTitle)
        val contentEditText = dialog.findViewById<EditText>(R.id.editContent)
        val add = dialog.findViewById<Button>(R.id.addButton)
        val cancel = dialog.findViewById<Button>(R.id.cancelButton)

        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        add.setOnClickListener {
            val title = titleEditText?.text.toString()
            val content = contentEditText?.text.toString()
            if(title.isNotEmpty() && content.isNotEmpty()){
                val timestamp = System.currentTimeMillis()
                val note = Note(title, content, timestamp)
                noteViewModel.insert(note)
                noteAdapter.notifyDataSetChanged()
                dialog.dismiss()
            } else{
                Toast.makeText(this,"Seems like you have missed something!",Toast.LENGTH_SHORT).show()
            }
        }
        cancel.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onItemClick(note: Note) {
        Toast.makeText(applicationContext,"Item Deleted..", Toast.LENGTH_SHORT).show()
    }
}