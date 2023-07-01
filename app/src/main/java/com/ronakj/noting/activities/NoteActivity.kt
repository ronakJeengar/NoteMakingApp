package com.ronakj.noting.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ronakj.noting.models.Note
import com.ronakj.noting.databinding.ActivityNoteBinding
import java.text.SimpleDateFormat
import java.util.Date

class NoteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNoteBinding

    private lateinit var note : Note
    private lateinit var old_note : Note
    private var isUpdate = false

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        try {
            old_note = intent.getSerializableExtra("current_note") as Note
            binding.etTitle.setText(old_note.title)
            binding.etNote.setText(old_note.note)
            isUpdate = true
        } catch (e : Exception){
            e.printStackTrace()
        }

        binding.saveBtn.setOnClickListener{
            val title = binding.etTitle.text.toString()
            val note_desc = binding.etNote.text.toString()

            if(title.isNotEmpty() || note_desc.isNotEmpty()){

                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")

                note = if(isUpdate){
                    Note(old_note.id,title,note_desc,formatter.format(Date()))
                } else{
                    Note(null,title,note_desc,formatter.format(Date()))
                }

                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK,intent)
                finish()
            } else{
                Toast.makeText(this@NoteActivity,"Seems like you missed something", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        binding.backBtn.setOnClickListener{
            onBackPressed()
        }
    }
}