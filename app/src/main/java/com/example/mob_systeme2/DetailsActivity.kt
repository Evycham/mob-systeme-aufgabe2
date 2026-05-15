package com.example.mob_systeme2

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mob_systeme2.data.TodoRepo
import com.example.mob_systeme2.model.Todo
import java.time.LocalDate
import android.app.DatePickerDialog
import android.view.View


class DetailsActivity : AppCompatActivity() {
    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var categoryInput: EditText
    private lateinit var priorityInput: EditText
    private lateinit var dueDateInput: EditText
    private lateinit var doneCheckBox: CheckBox
    private lateinit var deleteButton: Button
    private lateinit var saveButton: Button
    private lateinit var headlineView: TextView

    private var todoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bindViews()

        // todoId aus Intent (liefert von einem anderen Activity)
        todoId = intent.getStringExtra(EXTRA_TODO_ID)
        val todo = todoId?.let { TodoRepo.findTodo(it) }

        if(todoId != null && todo == null){
            showMessage("Todo was not finde.")
            finish()
            return
        }


        configureScreen(todo)
        deleteButton.setOnClickListener { deleteTodo() }
        saveButton.setOnClickListener { saveTodo(todo) }
        dueDateInput.setOnClickListener { openDataPicker() }
    }




    /**
     * Function to connect UI-components and components in logic
     * */
    private fun bindViews(){
        titleInput = findViewById(R.id.etTitle)
        descriptionInput = findViewById(R.id.etDescription)
        categoryInput = findViewById(R.id.etCategory)
        priorityInput = findViewById(R.id.etPriority)
        dueDateInput = findViewById(R.id.etDueDate)
        doneCheckBox = findViewById(R.id.cbDone)
        deleteButton = findViewById(R.id.btnDelete)
        saveButton = findViewById(R.id.btnSave)
        headlineView = findViewById(R.id.tvDetailsHeadline)
    }

    /**
     * Help function to show messages easily and flexible
     * @param message - message to be shown
     * */
    private fun showMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    /**
     * This obj must be classified but also should exist earlier than this class
     * */
    companion object{
        const val EXTRA_TODO_ID = "todo_id"
    }

    /**
     * Function to delete the chosen Todo
     * */
    private fun deleteTodo(){

        val id = todoId ?: return
        val removed = TodoRepo.deleteTodo(id)

        if(removed != null){
            showMessage(removed)
            return
        }

        showMessage("Todo was deleted.")
        finish()
    }


    /**
     * Function to creating or editing of todos
     * @param existingTodo - to prove if the todo already exists, if not create, otherwise edit
     * */
    private fun saveTodo(existingTodo: Todo?){

        val priority = priorityInput.text.toString().trim().toIntOrNull()
        if(priority == null){
            showMessage("Priority must be a number!")
            return
        }

        val dueDateText = dueDateInput.text.toString().trim()
        val dueDate = if(dueDateText.isEmpty()) null else LocalDate.parse(dueDateText)

        val title = titleInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()
        val category = categoryInput.text.toString().trim()
        val isDone = doneCheckBox.isChecked

        val status = if (existingTodo == null){
            TodoRepo.createTodo(title, description, priority, category, dueDate)
        } else{
            TodoRepo.editTodo(
                existingTodo.id,
                title,
                description,
                priority,
                category,
                isDone,
                dueDate
            )
        }
        showMessage(status)
        finish()
    }


    /**
     * Function for initiation of the date-picker menu.
     * */
    private fun openDataPicker(){
        // Start-wert für das Menu holen: if date existiert -> parsen, falls nein -> heutige nehmen
        val initialDate = dueDateInput.text.toString()
            .takeIf{ it.isNotBlank() }
            ?.let(LocalDate::parse)
            ?: LocalDate.now()

        DatePickerDialog(
            this,
            { _, year, month, day ->
                val selectedDate = LocalDate.of(year, month + 1, day)
                dueDateInput.setText(selectedDate.toString())
            },
            initialDate.year,
            initialDate.monthValue - 1,
            initialDate.dayOfMonth
        ).show()
    }

    /**
     * Function to configure users screen
     * @param todo - to be sure that we edit, otherwise create new
     * */
    private fun configureScreen(todo: Todo?){
        if(todo == null){
            headlineView.text = "New ToDo"
            doneCheckBox.visibility = View.GONE
            deleteButton.visibility = View.GONE
            return
        }

        headlineView.text = "ToDo Editing"
        titleInput.setText(todo.title)
        descriptionInput.setText(todo.description)
        categoryInput.setText(todo.category)
        priorityInput.setText(todo.priority.toString())
        dueDateInput.setText(todo.dueDate?.toString().orEmpty())
        doneCheckBox.isChecked = todo.done
    }
}