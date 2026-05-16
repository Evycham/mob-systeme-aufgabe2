package com.example.mob_systeme2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_systeme2.data.TodoRepo
import com.example.mob_systeme2.model.Todo


/**
 * Start screen of the app.
 *
 * This activity shows the list of all todos, allows sorting,
 * and opens the detail screen for creating or editing entries.
 */
class MainActivity : ComponentActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var underTitle: TextView
    private lateinit var prioritySortButton: Button
    private lateinit var deadlineSortButton: Button
    private lateinit var idSortButton: Button
    private lateinit var hintLabel: TextView
    private lateinit var todos: RecyclerView
    private lateinit var createButton: Button
    private lateinit var emptyState: TextView

    private lateinit var todoAdapter: TodoAdapter


    /**
     * Initializes the screen, binds the list adapter and registers all click listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        bindViews()

        // das ist ein konstruktor: Adapter erwatet eine Liste mit todos und auch für jedes task
        // setzen wir auch listener: clicken -> sofort methode editTodo(task)
        todoAdapter = TodoAdapter(TodoRepo.todos) { todo ->
            editTodo(todo)
        }
        // sagt dem RecyclerView, wie die Items dargestellt werden, *hier untereinander (vertikal)
        todos.layoutManager = LinearLayoutManager(this)
        todos.adapter = todoAdapter

        createButton.setOnClickListener { createTodo() }
        prioritySortButton.setOnClickListener {
            TodoRepo.sortTodo("byPriority")
            renderTodos()
        }
        deadlineSortButton.setOnClickListener {
            TodoRepo.sortTodo("byDeadline")
            renderTodos()
        }
        idSortButton.setOnClickListener {
            TodoRepo.sortTodo("byId")
            renderTodos()
        }
    }


    /**
     * Function to connect UI-components and components in logic
     * */
    private fun bindViews(){
        titleTextView = findViewById(R.id.tvMainHeadline)
        underTitle = findViewById(R.id.tvMainSubtitle)
        prioritySortButton = findViewById(R.id.btnSortPriority)
        deadlineSortButton = findViewById(R.id.btnSortDeadline)
        idSortButton = findViewById(R.id.btnSortId)
        hintLabel = findViewById(R.id.tvSectionLabel)
        todos = findViewById(R.id.rvTodos)
        createButton = findViewById(R.id.btnAddTodo)
        emptyState = findViewById(R.id.tvEmptyState)
    }

    /**
     * Opens the details screen in create mode.
     */
    private fun createTodo(){
        val intent = Intent(this, DetailsActivity::class.java)
        startActivity(intent)
    }

    /**
     * Opens the details screen in edit mode for the selected todo.
     *
     * @param todo todo that should be edited
     */
    private fun editTodo(todo: Todo){
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.EXTRA_TODO_ID, todo.id)
        startActivity(intent)
    }

    /**
     * Refreshes the RecyclerView and updates the empty-state message.
     */
    private fun renderTodos(){
        todoAdapter.updateTodos(TodoRepo.todos)
        emptyState.visibility = if(TodoRepo.todos.isEmpty()) View.VISIBLE else View.GONE
    }

    /**
     * Reloads the list whenever the activity becomes active again.
     */
    override fun onResume() {
        super.onResume()
        renderTodos()
    }
}
