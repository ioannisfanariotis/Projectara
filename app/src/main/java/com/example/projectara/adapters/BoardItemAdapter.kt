package com.example.projectara.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectara.R
import com.example.projectara.databinding.BoardItemBinding
import com.example.projectara.models.Board

open class BoardItemAdapter(private val context: Context, private var list: ArrayList<Board>):  RecyclerView.Adapter<BoardItemAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    class ViewHolder(binding: BoardItemBinding): RecyclerView.ViewHolder(binding.root){
        val image = binding.rvImage
        val name = binding.rvName
        val creator = binding.rvCreator
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(BoardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        if(holder is ViewHolder){
            Glide.with(context).load(model.image).placeholder(R.drawable.ic_board_place_holder).into(holder.image)
            holder.name.text = model.name
            holder.creator.text = "Created by: ${model.createdBy}"

            holder.itemView.setOnClickListener {
                if (onClickListener != null){
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickListener{
        fun onClick(position: Int, model: Board)
    }
}