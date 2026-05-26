package com.example.mob_systeme2.data

import android.content.Context
import com.example.mob_systeme2.data.local.AppDatabase
import com.example.mob_systeme2.data.local.TodoDao
import com.example.mob_systeme2.model.Todo
import java.time.LocalDate
import java.util.UUID
import kotlin.comparisons.nullsLast


/**
 * Darf nur eine Instanz erzeugt werden, quasi global. Kann einfach überall zugreifen.
 * */
object TodoRepo {
    private var todoDao: TodoDao? = null

    fun init(context: Context) {
        if (todoDao == null) {
            todoDao = AppDatabase.getInstance(context).todoDao()
        }
    }

    private fun dao(): TodoDao = requireNotNull(todoDao) { "TodoRepo is not initialized. Call TodoRepo.init(context) first." }

    /**
     * Toggle counter for deadline sorting direction.
     */
    var countDeadline = 0

    /**
     * Toggle counter for priority sorting direction.
     */
    var countPriority = 0

    /**
     * Toggle counter for id sorting direction.
     */
    var countId = 0

    fun getTodos(): List<Todo> {
        val list = dao().getAll().toMutableList()
        when (sortType) {
            "byPriority" -> if (ascendingSort) list.sortBy { it.priority } else list.sortByDescending { it.priority }
            "byDeadline" -> {
                if (ascendingSort) {
                    list.sortWith(compareBy(nullsLast()) { it.dueDate })
                } else {
                    list.sortWith(compareBy(nullsFirst(reverseOrder())) { it.dueDate })
                }
            }
            "byId" -> if (ascendingSort) list.sortBy { it.id } else list.sortByDescending { it.id }
        }
        return list
    }

    /**
     * Function to create a task
     *
     * @param title - title can not be empty
     * @param description - description
     * @param priority - priority (from 1 to 3)
     * @param categoryIds - each task must contain a category
     * @param dueDate - the DeadLine
     *
     * @return null if the task was successful created
     * @return String if something is wrong
     * */
    fun createTodo(
        title: String,
        description: String,
        priority: Int,
        categoryIds: Set<String>,
        dueDate: LocalDate?
    ): String?{

        if(title.isBlank()) return "Title can not be empty!"

        val id = UUID.randomUUID().toString()
        val todo = Todo(
            id = id,
            title = title,
            description = description,
            priority = priority,
            categoryIds = categoryIds.toMutableSet(),
            done = false,
            dueDate = dueDate
        )

        dao().insert(todo)

        return null
    }


    /**
     * Function to remove a task
     *
     * @param id - id of the task
     *
     * @return null if the task was successful removed
     * @return String if there is no task with such id
     * */
    fun deleteTodo(id: String): String?{
        val todo = dao().findById(id) ?: return "There is no such a Todo!"
        dao().delete(todo)

        return null
    }


    /**
     * Find task with id
     *
     * @param id - id
     *
     * @return task - successful
     * @return null - did not find
     * */
    fun findTodo(id: String): Todo?{
        return dao().findById(id)
    }


    /**
     * Function to edit the existed task
     * @param id - id from the task which has to be changed
     * @param newTitle - new title
     * @param newDescription - new description
     * @param newPriority - new priority
     * @param newCategoryIds - new category
     * @param newDueDate - new deadline
     *
     * @return null - successful
     * @return String - something is wrong
     * */
    fun editTodo(
        id: String,
        newTitle: String,
        newDescription: String,
        newPriority: Int,
        newCategoryIds: Set<String>,
        isDone: Boolean,
        newDueDate: LocalDate?
    ): String?{

        val oldTodo: Todo = findTodo(id) ?: return "There is no such a ToDo!"
        if(newTitle.isBlank()) return "Title can not be empty!"

        if(newTitle != oldTodo.title) oldTodo.title = newTitle
        if(newDescription != oldTodo.description) oldTodo.description = newDescription
        if(newPriority != oldTodo.priority) oldTodo.priority = newPriority

        if(newCategoryIds != oldTodo.categoryIds){
            oldTodo.categoryIds.clear()
            oldTodo.categoryIds.addAll(newCategoryIds)
        }

        if (newDueDate != oldTodo.dueDate) oldTodo.dueDate = newDueDate
        if (isDone != oldTodo.done) oldTodo.done = isDone

        dao().update(oldTodo)

        return null
    }

/**
 * Function for the sorting of todos.
 * @param type : byPriority
 * @param type : byDeadline
 * @param type: byID
 * */
    fun sortTodo(type: String){
        when (type) {
            "byPriority" -> sortPriority()
            "byDeadline" -> sortDeadline()
            "byId" -> sortId()
        }
    }

    private var sortType = "byId"
    private var ascendingSort = true

    /**
     * Sorts todos by priority and alternates between ascending and descending order.
     */
    fun sortPriority(){
        sortType = "byPriority"
        ascendingSort = countPriority % 2 == 0
        countPriority++
    }

    /**
     * Sorts todos by deadline and alternates between ascending and descending order.
     *
     * Todos without a deadline are placed last in ascending order
     * and first in descending order.
     */
    fun sortDeadline(){
        sortType = "byDeadline"
        ascendingSort = countDeadline % 2 == 0
        countDeadline++
    }

    /**
     * Sorts todos by id and alternates between ascending and descending order.
     */
    fun sortId(){
        sortType = "byId"
        ascendingSort = countId % 2 == 0
        countId++
    }


}
