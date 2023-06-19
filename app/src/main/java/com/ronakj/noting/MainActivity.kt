package com.ronakj.noting

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ronakj.noting.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var fabAddNote: FloatingActionButton

    private val notes = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        recyclerView = binding?.recyclerView!!
        fabAddNote = binding?.addNote!!

        noteAdapter = NoteAdapter(notes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = noteAdapter

        fabAddNote.setOnClickListener {
            showAddNoteDialog()
        }
    }

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
                val note = Note(notes.size + 1, title, content, timestamp)
                notes.add(note)
                noteAdapter.notifyItemInserted(notes.size - 1)
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
}