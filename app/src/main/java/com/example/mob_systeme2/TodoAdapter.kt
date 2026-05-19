package com.example.mob_systeme2;

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View;
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_systeme2.model.Todo;
import java.time.LocalDate

/**
 * RecyclerView adapter for rendering todos inside the list on the main screen.
 *
 * @param todoList current todos that should be displayed
 * @param onTodoClick callback invoked when the user taps a todo item
 */
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

    /**
     * Binds the todo data for the current position to the visible item views.
     *
     * @param holder holder containing the item views
     * @param position index of the todo inside [todoList]
     * */
    override fun onBindViewHolder(
        holder: TodoViewHolder,
        position: Int
    ) {
        val todo = todoList[position]

        holder.titleView.text = todo.title
        holder.descriptionView.text = todo.description.ifBlank { "No description" }
        holder.categoryView.text = todo.category.ifBlank { "No category" }
        holder.priorityView.text = "Priority: ${todo.priority}"

        // Wenn dueDate nicht null ist, wandle zu String um; sonst "without"
        holder.deadlineView.text = "Deadline: ${todo.dueDate?.toString() ?: "without"}"

        // da dueDate ein var ist, sagt Kotlin: vielleicht ist es beim zweiten Zugriff schon wieder null
        // deswegen braucht man eine val hier
        val dueDate = todo.dueDate
        if (dueDate != null && dueDate.isBefore(LocalDate.now()) && !todo.done) {
            holder.deadlineView.setTextColor(Color.parseColor("#EF6C00"))
        } else {
            holder.deadlineView.setTextColor(Color.DKGRAY)
        }

        if(todo.done) {
            holder.statusView.text = "Done"
            holder.statusView.setTextColor(Color.parseColor("#C62828"))
        } else{
            holder.statusView.text = "Open"
            holder.statusView.setTextColor(Color.parseColor("#2E7D32"))
        }
        holder.itemView.setOnClickListener {
            onTodoClick(todo)
        }
    }

    /**
     * Returns the number of todos currently shown by the adapter.
     */
    override fun getItemCount(): Int = todoList.size

    /**
     * Replaces the displayed todos and refreshes the list UI.
     *
     * @param newTodos new list that should be rendered
     */
    fun updateTodos(newTodos: List<Todo>){
        todoList = newTodos
        // RecyclerView aktualisiert Bildschirm.
        notifyDataSetChanged()
    }

}
