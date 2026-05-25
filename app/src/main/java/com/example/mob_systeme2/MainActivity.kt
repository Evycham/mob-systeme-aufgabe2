package com.example.mob_systeme2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_systeme2.adapters.TodoAdapter
import com.example.mob_systeme2.data.CategoryRepo
import com.example.mob_systeme2.data.TodoRepo
import com.example.mob_systeme2.model.TodoCategory
import com.example.mob_systeme2.model.Todo
import com.example.mob_systeme2.sheets.CategoryFilterBottomSheet
import com.example.mob_systeme2.sheets.EditCategoryBottomSheet


/**
 * Start screen of the app.
 *
 * This activity shows the list of all todos, allows sorting,
 * and opens the detail screen for creating or editing entries.
 */
class MainActivity : AppCompatActivity(), CategoryFilterBottomSheet.Callback, EditCategoryBottomSheet.Callback {

    private lateinit var titleTextView: TextView
    private lateinit var underTitle: TextView
    private lateinit var prioritySortButton: Button
    private lateinit var deadlineSortButton: Button
    private lateinit var idSortButton: Button
    private lateinit var hintLabel: TextView
    private lateinit var todos: RecyclerView
    private lateinit var categories: RecyclerView
    private lateinit var createButton: Button
    private lateinit var categoryFilterButton: Button
    private lateinit var emptyState: TextView

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var categoryAdapter: MainCategoryAdapter
    private val selectedFilterCategoryIds = mutableSetOf<String>()


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
        todoAdapter = TodoAdapter(TodoRepo.getTodos()) { todo ->
            editTodo(todo)
        }
        // sagt dem RecyclerView, wie die Items dargestellt werden, *hier untereinander (vertikal)
        todos.layoutManager = LinearLayoutManager(this)
        todos.adapter = todoAdapter

        categoryAdapter = MainCategoryAdapter(
            categories = CategoryRepo.getCategories(),
            onCategoryClick = { category ->
                openCategoryEditor(category.id)
            }
        )
        categories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        categories.adapter = categoryAdapter

        createButton.setOnClickListener { createTodo() }
        categoryFilterButton.setOnClickListener { openCategoryFilterSheet() }
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
        categories = findViewById(R.id.rvCategories)
        createButton = findViewById(R.id.btnAddTodo)
        categoryFilterButton = findViewById(R.id.btnOpenCategoryFilters)
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
        val allTodos = TodoRepo.getTodos()
        val filtered = if (selectedFilterCategoryIds.isEmpty()) {
            allTodos
        } else {
            allTodos.filter { todo ->
                todo.categoryIds.any { selectedFilterCategoryIds.contains(it) }
            }
        }
        todoAdapter.updateTodos(filtered)
        emptyState.visibility = if(filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun renderCategories() {
        categoryAdapter.updateCategories(CategoryRepo.getCategories())
    }

    private fun openCategoryFilterSheet() {
        CategoryFilterBottomSheet.newInstance(selectedFilterCategoryIds)
            .show(supportFragmentManager, "category_filter_sheet")
    }

    private fun openCategoryEditor(categoryId: String?) {
        EditCategoryBottomSheet.newInstance(categoryId)
            .show(supportFragmentManager, "edit_category_sheet")
    }

    override fun onApplyCategoryFilter(selectedIds: Set<String>) {
        selectedFilterCategoryIds.clear()
        selectedFilterCategoryIds.addAll(selectedIds)
        renderTodos()
    }

    override fun onOpenCategoryEditor(categoryId: String?) {
        openCategoryEditor(categoryId)
    }

    override fun onCategoryChanged() {
        renderCategories()
        renderTodos()
    }

    /**
     * Reloads the list whenever the activity becomes active again.
     */
    override fun onResume() {
        super.onResume()
        renderCategories()
        renderTodos()
    }
}

private class MainCategoryAdapter(
    private var categories: List<TodoCategory>,
    private val onCategoryClick: (TodoCategory) -> Unit
) : RecyclerView.Adapter<MainCategoryAdapter.MainCategoryViewHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): MainCategoryViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_main, parent, false)
        return MainCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainCategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.name
        holder.categoryIcon.text = category.iconKey?.firstOrNull()?.uppercase() ?: "•"
        holder.itemView.setOnClickListener { onCategoryClick(category) }
    }

    override fun getItemCount(): Int = categories.size

    fun updateCategories(newCategories: List<TodoCategory>) {
        categories = newCategories
        notifyDataSetChanged()
    }

    class MainCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryIcon: TextView = itemView.findViewById(R.id.tvCategoryIcon)
        val categoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
    }
}
