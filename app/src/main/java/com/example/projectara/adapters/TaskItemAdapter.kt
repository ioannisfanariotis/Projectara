package com.example.projectara.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.projectara.R
import com.example.projectara.databinding.ItemBoardBinding
import com.example.projectara.databinding.ItemTaskBinding
import com.example.projectara.models.Task

open class TaskItemAdapter(private val context: Context, private var list: ArrayList<Task>):  RecyclerView.Adapter<TaskItemAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root){
        val addList = binding.addList
        val llTaskItem = binding.llTaskItem
        val taskListTitle = binding.taskListTitle
        val addTaskListName = binding.addTaskListName
        val closeList = binding.closeList
        val doneList = binding.doneList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false)
        val layoutParams = LinearLayout.LayoutParams((parent.width * 0.7).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins((15.toDp().toPx()), 0, (40.toDp()).toPx(), 0)
        view.layoutParams = layoutParams
        return ViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        if(holder is ViewHolder){
            if (position == list.size - 1){
                holder.addList.visibility = View.VISIBLE
                holder.llTaskItem.visibility = View.GONE
            }else{
                holder.addList.visibility = View.GONE
                holder.llTaskItem.visibility = View.VISIBLE
            }
            holder.taskListTitle.text = model.title
            holder.addList.setOnClickListener {
                holder.addList.visibility = View.GONE
                holder.addTaskListName.visibility = View.VISIBLE
            }
            holder.closeList.setOnClickListener {
                holder.addList.visibility = View.VISIBLE
                holder.addTaskListName.visibility = View.GONE
            }
            holder.doneList.setOnClickListener{

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun Int.toDp(): Int = (this/ Resources.getSystem().displayMetrics.density).toInt()

    private fun Int.toPx(): Int = (this* Resources.getSystem().displayMetrics.density).toInt()
}