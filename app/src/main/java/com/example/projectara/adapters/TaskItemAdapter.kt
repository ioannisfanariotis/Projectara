package com.example.projectara.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectara.databinding.ItemTaskBinding
import com.example.projectara.models.Task

open class TaskItemAdapter(private val context: Context, private var list: ArrayList<Task>):  RecyclerView.Adapter<TaskItemAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return list.size
    }
}