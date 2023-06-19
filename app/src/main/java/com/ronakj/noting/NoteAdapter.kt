package com.ronakj.noting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(private val notes : List<Note>):RecyclerView.Adapter<NoteAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content
        holder.timeStampTextView.text = note.timeStamp.toString()


    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val contentTextView: TextView = itemView.findViewById(R.id.content)
        val timeStampTextView: TextView = itemView.findViewById(R.id.timeStamp)

    }
}