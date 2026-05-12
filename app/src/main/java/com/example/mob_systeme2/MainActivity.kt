package com.example.mob_systeme2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_systeme2.model.Todo


class MainActivity : ComponentActivity() {

    private lateinit var titel: TextView
    private lateinit var unterTitel: TextView
    private lateinit var prioritySortButton: Button
    private lateinit var deadlineSortButton: Button
    private lateinit var idSortButton: Button
    private lateinit var hintLabel: TextView
    private lateinit var todos: RecyclerView
    private lateinit var createButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        bindViews()
    }


    /**
     * Function to connect UI-components and components in logic
     * */
    private fun bindViews(){
        titel = findViewById(R.id.tvMainHeadline)
        unterTitel = findViewById(R.id.tvMainSubtitle)
        prioritySortButton = findViewById(R.id.btnSortPriority)
        deadlineSortButton = findViewById(R.id.btnSortDeadline)
        idSortButton = findViewById(R.id.btnSortId)
        hintLabel = findViewById(R.id.tvSectionLabel)
        todos = findViewById(R.id.rvTodos)
        createButton = findViewById(R.id.btnAddTodo)
    }

    private fun createTodo(todo: Todo?){
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.EXTRA_TODO_ID, todoId)
        startActivity(intent)
    }

}