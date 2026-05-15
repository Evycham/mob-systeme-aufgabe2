package com.example.mob_systeme2.data

import com.example.mob_systeme2.model.Todo
import java.time.LocalDate
import java.util.UUID
import kotlin.comparisons.nullsLast


/**
 * Darf nur eine Instanz erzeugt werden, quasi global. Kann einfach überall zugreifen.
 * */
object TodoRepo {
    val todos = mutableListOf<Todo>()

    var countDeadline = 0
    var countPriority = 0
    var countId = 0

    /**
     * Function to create a task
     *
     * @param title - title can not be empty
     * @param description - description
     * @param priority - priority (from 1 to 3)
     * @param category - each task must contain a category
     * @param dueDate - the DeadLine
     *
     * @return null if the task was successful created
     * @return String if something is wrong
     * */
    fun createTodo(
        title: String,
        description: String,
        priority: Int,
        category: String,
        dueDate: LocalDate?
    ): String?{
        if(title.isBlank()) return "Title can not be empty!"
        if(priority !in 1..3) return "Priority has to be in the range from 1 to 3!"
        if(category.isBlank()) return "Todo must have Category!"

        val id = UUID.randomUUID().toString()
        val todo = Todo(id, title, description, priority, category, done = false, dueDate)

        todos.add(todo)

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
        // gehen ganzes todos-List durch und wenn id gleich zu gesuchten ist, dann löschen
        val removed = todos.removeIf { it.id == id }

        return if(removed) null else "There is no such a Todo!"
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
        return todos.find { it.id == id }
    }


    /**
     * Function to edit the existed task
     * @param id - id from the task which has to be changed
     * @param newTitle - new title
     * @param newDescription - new description
     * @param newPriority - new priority
     * @param newCategory - new category
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
        newCategory: String,
        isDone: Boolean,
        newDueDate: LocalDate?
    ): String?{

        val todo: Todo = findTodo(id) ?: return "There is no such a ToDo!"

        if(newTitle.isBlank()) return "Title can not be empty!"
        if(newPriority !in 1..3) return "Priority has to be in the range from 1 to 3!"
        if(newCategory.isBlank()) return "Todo must have Category!"

        if(newTitle != todo.title) todo.title = newTitle
        if(newDescription != todo.description) todo.description = newDescription
        if(newPriority != todo.priority) todo.priority = newPriority
        if(newCategory != todo.category) todo.category = newCategory
        if(newDueDate != todo.dueDate) todo.dueDate = newDueDate
        if(isDone != todo.done) todo.done = isDone

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

    fun sortPriority(){
        if(countPriority % 2 == 0){
            todos.sortBy { it.priority }
        } else{
            todos.sortByDescending { it.priority }
        }
        countPriority++
    }

    fun sortDeadline(){
        if(countDeadline % 2 == 0){
            todos.sortWith(compareBy(nullsLast()) { it.dueDate })
        } else{
            todos.sortWith(compareBy(nullsFirst(reverseOrder())) {it.dueDate})
        }
        countDeadline++
    }

    fun sortId(){
        if(countId % 2 == 0){
            todos.sortBy { it.id }
        } else{
            todos.sortByDescending { it.id }
        }
        countId++
    }
}
