package com.ronakj.noting.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ronakj.noting.models.Note
import com.ronakj.noting.R
import kotlin.random.Random

class NoteAdapter(private val context: Context, private val listener: NotesItemClickListener) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val notesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_note, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    private fun randomColor() : Int{
        val list = ArrayList<Int>()
        list.add(R.color.NoteColor1)
        list.add(R.color.NoteColor2)
        list.add(R.color.NoteColor3)
        list.add(R.color.NoteColor4)
        list.add(R.color.NoteColor5)
        list.add(R.color.NoteColor6)

        val seed = System.currentTimeMillis().toInt()

        val randomIndex = Random(seed).nextInt(list.size)

        return list[randomIndex]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList : List<Note>){
        fullList.clear()
        fullList.addAll(newList)

        notesList.clear()
        notesList.addAll(fullList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(search : String){
        notesList.clear()
        for (item in fullList){
            if(item.title?.lowercase()?.contains(search.lowercase()) == true || item.note?.lowercase()?.contains(search.lowercase()) == true){
                notesList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val currentNote = notesList[position]
        holder.title!!.text = currentNote.title
        holder.title.isSelected = true
        holder.note!!.text = currentNote.note
        holder.date!!.text = currentNote.date
        holder.date.isSelected = true

//        holder.cardLayout?.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(), null))

        holder.cardLayout?.setOnClickListener{
            listener.onItemClicked(notesList[holder.adapterPosition])
        }

        holder.cardLayout?.setOnLongClickListener{
            listener.onLongItemClicked(notesList[holder.adapterPosition], holder.cardLayout)
            true
        }
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val cardLayout : CardView? = itemView.findViewById(R.id.cardLayout)
        val title : TextView? = itemView.findViewById(R.id.tv_title)
        val note : TextView? = itemView.findViewById(R.id.tv_note)
        val date : TextView? = itemView.findViewById(R.id.tv_date)

    }

    interface NotesItemClickListener{
        fun onItemClicked(note: Note)
        fun onLongItemClicked(note: Note, cardView: CardView)
    }
}