package com.ronakj.noting.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ronakj.noting.R
import com.ronakj.noting.adapter.NoteAdapter
import com.ronakj.noting.databinding.ActivityMainBinding
import com.ronakj.noting.db.NoteDatabase
import com.ronakj.noting.models.Note
import com.ronakj.noting.models.NoteViewModel

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), NoteAdapter.NotesItemClickListener,
    PopupMenu.OnMenuItemClickListener {

    private var binding : ActivityMainBinding? = null
    private lateinit var database : NoteDatabase
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var selectedNote : Note

    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == Activity.RESULT_OK){

            val note = result.data?.getSerializableExtra("note") as? Note

            if(note != null) {
                viewModel.updateNote(note)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)

        initUI()
        viewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application))[NoteViewModel::class.java]

        viewModel.allNotes.observe(this){ list ->
            list?.let {
                adapter.updateList(list)
            }
        }
        database = NoteDatabase.getDatabase(this)
    }

    private fun initUI() {

        binding?.recyclerView?.setHasFixedSize(true)
        binding?.recyclerView?.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NoteAdapter(this, this)
        binding?.recyclerView?.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){

                val note = result.data?.getSerializableExtra("note") as? Note
                if(note != null) {
                    viewModel.insertNote(note)
                }
            }
        }

        binding?.addNote?.setOnClickListener{
            val intent = Intent(this, NoteActivity::class.java)
            getContent.launch(intent)
        }

        var isGridView = false

        binding?.viewChange?.setOnClickListener {
            isGridView = !isGridView
            val imageResource = if (isGridView) R.drawable.baseline_grid_view_24 else R.drawable.baseline_view_stream_24
            val layoutManager = if (isGridView) LinearLayoutManager(this) else StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)

            binding?.viewChange?.setImageResource(imageResource)
            binding?.recyclerView?.layoutManager = layoutManager
        }

        binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null){
                    adapter.filterList(newText)
                }
                return true
            }
        })
    }

    override fun onItemClicked(note: Note) {
        val intent = Intent(this@MainActivity, NoteActivity::class.java)
        intent.putExtra("current_note", note)
        updateNote.launch(intent)
    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.delete_popup)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        if(item?.itemId == R.id.deleteNote){
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }
}