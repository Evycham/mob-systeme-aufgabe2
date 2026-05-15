package com.example.mob_systeme2;

import android.view.LayoutInflater
import android.view.View;
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_systeme2.model.Todo;

class TodoAdapter(
    private var todoList: List<Todo>,
    private val onTodoClick: (Todo) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    /**
     * My "template" for all todos. When Android builds a view -> ViewHolder grabs everything what
     * are important and memorizes
     * @param itemView - my whole XML-file (item_todo.xml)
     * */
    class TodoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val titleView: TextView = itemView.findViewById(R.id.tvTodoTitle)
        val descriptionView: TextView = itemView.findViewById(R.id.tvTodoDescription)
        val categoryView: TextView = itemView.findViewById(R.id.tvTodoCategory)
        val priorityView: TextView = itemView.findViewById(R.id.tvTodoPriority)
        val deadlineView: TextView = itemView.findViewById(R.id.tvTodoDeadline)
        val statusView: TextView = itemView.findViewById(R.id.tvTodoStatus)
    }

    /**
     * Here will be crated visible View which will be sent to TodoViewHolder
     * @param parent - RecyclerView or Container for size/layout
     * @return TodoViewHolder - class with connected items
     * */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TodoViewHolder,
        position: Int
    ) {
        val todo = todoList[position]

        holder.titleView.text = todo.title
        holder.descriptionView.text = todo.description.ifBlank { "No description" }
        holder.priorityView.text = todo.category
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}