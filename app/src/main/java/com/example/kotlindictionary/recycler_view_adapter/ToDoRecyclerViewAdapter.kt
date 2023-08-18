package com.example.kotlindictionary.recycler_view_adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlindictionary.databinding.RecyclerViewRowBinding
import com.example.kotlindictionary.room_database_todo.TODO

class ToDoRecyclerViewAdapter(var todoArrayList:ArrayList<TODO>) : RecyclerView.Adapter<ToDoRecyclerViewAdapter.TODOAdapter>() {

    var backgroundColors= arrayListOf<String>("#3fb8af","#7fc7af","#dad8a7","#ff9e9d","#ff3d7f")

    class TODOAdapter(val binding : RecyclerViewRowBinding) :RecyclerView.ViewHolder(binding.root) {   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TODOAdapter {
        val view=RecyclerViewRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TODOAdapter(view)
    }

    override fun getItemCount(): Int {
        return todoArrayList.size
    }

    override fun onBindViewHolder(holder: TODOAdapter, position: Int) {
        holder.binding.recyclerViewLayout.setBackgroundColor(Color.parseColor(backgroundColors[position%5]))
        holder.binding.recyclerViewRowComment.text=todoArrayList[position].comment
        holder.binding.recyclerViewRowDate.text=todoArrayList[position].date
        holder.binding.recyclerViewRowTime.text=todoArrayList[position].time
    }
}