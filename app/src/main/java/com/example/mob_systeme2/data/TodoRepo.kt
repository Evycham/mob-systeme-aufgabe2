package com.example.mob_systeme2.data

import com.example.mob_systeme2.model.Todo
import java.util.UUID


/**
 * Darf nur eine Instanz erzeugt werden, quasi global. Kann einfach überall zugreifen.
 * */
object TodoRepo {
    val todos = mutableListOf<Todo>()


    /**
     * Funktion to create a task
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
        dueDate: String
    ): String?{
        if(title.isBlank()) return "Titel darf nicht leer sein"
        if(priority !in 1..3) return "Priority is invalid"
        if(category.isBlank()) return "Todo must have Category"

        val id = todos.size + 1
        val todo = Todo(id, title, description, priority, category, done = false, dueDate)

        todos.add(todo)

        return null
    }

    /**
     * Funktion to remove a task
     *
     * @param id - id of the task
     *
     * @return null if the task was successful removed
     * @return String if there is no task with such id
     * */
    fun removeTodo(id: Int): String?{
        // gehen ganzes todos-List durch und wenn id gleich zu gesuchten ist, dann löschen
        val removed = todos.removeIf { it.id == id }

        return if(removed) null else return "There is no such a Todo!"
    }

    /**
     * Find task with id
     *
     * @param id - id
     *
     * @return todo - successful
     * @return null - did not find
     * */
    fun findTodo(id: Int): Todo?{
        return todos.find { it.id == id }
    }


    fun editTodo(
        id: Int,
        newTitle: String,
        newDescription: String,
        newPriority: Int,
        newCategory: String,
        newDueDate: String
    ): String?{

        val todo: Todo = findTodo(id) ?: return "Something is wrong"

        if(newTitle.isBlank()) return "Titel darf nicht leer sein"
        if(newPriority !in 1..3) return "Priority is invalid"
        if(newCategory.isBlank()) return "Todo must have Category"

        if(newTitle != todo.title) todo.title = newTitle
        if(newDescription != todo.description) todo.description = newDescription
        if(newPriority != todo.priority) todo.priority = newPriority
        if(newCategory != todo.category) todo.category = newCategory



        return null
    }
}