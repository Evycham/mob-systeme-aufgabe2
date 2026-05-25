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
import android.media.MediaPlayer
import android.view.View
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView


/**
 * Screen for creating a new todo or editing an existing one.
 *
 * It validates user input, persists changes through [TodoRepo]
 * and can play a short sound when a todo is marked as done.
 */
class DetailsActivity : AppCompatActivity(), SelectCategoriesBottomSheet.Callback {
    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var categoryInput: EditText
    private lateinit var priorityInput: Spinner
    private lateinit var dueDateInput: EditText
    private lateinit var doneCheckBox: CheckBox
    private lateinit var deleteButton: Button
    private lateinit var saveButton: Button
    private lateinit var categoryButton: Button
    private lateinit var categoryWindow: RecyclerView
    private lateinit var headlineView: TextView
    private var donePlayer: MediaPlayer? = null

    private var todoId: String? = null

    private var selectedCategoryIds = mutableSetOf<String>()

    /**
     * Sets up the detail screen, loads the todo from the intent and binds UI actions.
     */
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
        todo?.let{ selectedCategoryIds = todo.categoryIds } ?: emptySet<String>()

        if(todoId != null && todo == null){
            showMessage("Todo was not find.")
            finish()
            return
        }


        configureScreen(todo)
        donePlayer = MediaPlayer.create(this, R.raw.fahhhhh)
        deleteButton.setOnClickListener { deleteTodo() }
        saveButton.setOnClickListener { saveTodo(todo) }
        dueDateInput.setOnClickListener { openDataPicker() }
        categoryButton.setOnClickListener { openCategoryPicker() }
    }




    /**
     * Function to connect UI-components and components in logic
     * */
    private fun bindViews(){
        titleInput = findViewById(R.id.etTitle)
        descriptionInput = findViewById(R.id.etDescription)
        categoryInput = findViewById(R.id.etCategory)
        priorityInput = findViewById(R.id.spPriority)
        dueDateInput = findViewById(R.id.etDueDate)
        doneCheckBox = findViewById(R.id.cbDone)
        deleteButton = findViewById(R.id.btnDelete)
        saveButton = findViewById(R.id.btnSave)
        headlineView = findViewById(R.id.tvDetailsHeadline)
        categoryButton = findViewById(R.id.btnOpenCategoryPicker)
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
        /**
         * Intent extra key used to pass the id of the todo that should be edited.
         */
        const val EXTRA_TODO_ID = "todo_id"
    }

    /**
     * Function to delete the chosen taskEXTRA_TODO_ID
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
     * @param existingTodo - to prove if the task already exists, if not create, otherwise edit
     * */
    private fun saveTodo(existingTodo: Todo?){

        val priority = priorityInput.selectedItem.toString().toInt()

        val dueDateText = dueDateInput.text.toString().trim()
        val dueDate = if (dueDateText.isEmpty()) {
            null
        } else {
            val parsedDate = try {
                LocalDate.parse(dueDateText)
            } catch (e: Exception){
                showMessage("Please enter a valid date!")
                return
            }

            if (parsedDate.isBefore(LocalDate.now())) {
                showMessage("Deadline can not be earlier than today!")
                return
            }

            parsedDate
        }

        val title = titleInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()
        val category = categoryInput.text.toString().trim()
        val isDone = doneCheckBox.isChecked

        val previousIsDone = existingTodo?.done

        val errorMessage = if (existingTodo == null){
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

        if (errorMessage != null) {
            showMessage(errorMessage)
            return
        }

        if(existingTodo == null){
            showMessage("Todo was successful created!")
        } else {
            showMessage("Successful saved!")
            if(!previousIsDone!! && isDone ) playDoneSound()
        }
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

        // Datum holen
        DatePickerDialog(
            this,
            { _, year, month, day ->
                val selectedDate = LocalDate.of(year, month + 1, day)
                dueDateInput.setText(selectedDate.toString())
            },
            // Ersetzen
            initialDate.year,
            initialDate.monthValue - 1,
            initialDate.dayOfMonth
        ).show()
    }

    /**
     * Function to configure users screen
     * @param task - to ensure that we edit, otherwise create new
     * */
    private fun configureScreen(task: Todo?){
        if(task == null){
            headlineView.text = "New ToDo"
            doneCheckBox.visibility = View.GONE
            deleteButton.visibility = View.GONE
            priorityInput.setSelection(0)
            return
        }

        headlineView.text = "ToDo Editing"
        titleInput.setText(task.title)
        descriptionInput.setText(task.description)
        categoryInput.setText(task.category)
        priorityInput.setSelection(task.priority - 1)
        dueDateInput.setText(task.dueDate?.toString().orEmpty())
        doneCheckBox.isChecked = task.done
    }

    /**
     * Plays the completion sound from the beginning.
     */
    private fun playDoneSound(){
        // let nimmt donePlayer und gibt ihn im Block als player
        donePlayer?.let{ player ->
            player.seekTo(0)
            player.start()
        // wenn donePlayer null -> einfach finish()
        } ?: finish()
    }

    private fun openCategoryPicker(){
        SelectCategoriesBottomSheet.newInstance(selectedCategoryIds).show(supportFragmentManager, "select_categories")
    }


    override fun onCategoiresPicked(ids: Set<String>){
        selectedCategoryIds.clear()
        selectedCategoryIds.addAll(ids)
        rendernSelectedCategories()
    }

    private fun rendernSelectedCategories(){

    }
}
