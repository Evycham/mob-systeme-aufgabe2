package com.example.mob_systeme2.data

import android.content.Context
import com.example.mob_systeme2.data.local.AppDatabase
import com.example.mob_systeme2.data.local.CategoryDao
import com.example.mob_systeme2.data.local.TodoDao
import com.example.mob_systeme2.model.TodoCategory
import java.util.UUID

/**
 * In-memory repository for category management.
 *
 * This object stores all categories for the current app process and offers
 * basic CRUD operations (create, read, update, delete).
 */
object CategoryRepo {

    /**
     * Current list of all categories.
     *
     * Note: This list is kept only in memory and is reset when the app process restarts.
     */
    private var categoryDao: CategoryDao? = null
    private var todoDao: TodoDao? = null

    fun init(context: Context) {
        if (categoryDao == null || todoDao == null) {
            val db = AppDatabase.getInstance(context)
            categoryDao = db.categoryDao()
            todoDao = db.todoDao()
        }
    }

    private fun cDao(): CategoryDao = requireNotNull(categoryDao) { "CategoryRepo is not initialized. Call CategoryRepo.init(context) first." }
    private fun tDao(): TodoDao = requireNotNull(todoDao) { "CategoryRepo is not initialized. Call CategoryRepo.init(context) first." }

    fun getCategories(): List<TodoCategory> = cDao().getAll()

    /**
     * Creates a new category and adds it to [categoryList].
     *
     * @param id Unused input parameter in current implementation.
     * @param name Visible category name. Must not be blank.
     * @param colorKey Optional key that points to a predefined color preset.
     * @param iconKey Optional key that points to a predefined icon preset.
     * @return `null` if creation succeeds, otherwise an error message.
     */
    fun createCategory(
        name: String,
        colorKey: String?,
        iconKey: String?
    ): String?{

        if(name.isBlank()) return "Name can not be empty!"

        val categoryId = UUID.randomUUID().toString()
        val category = TodoCategory(categoryId, name, colorKey, iconKey)

        cDao().insert(category)

        return null
    }

    /**
     * Deletes an existing category by id.
     *
     * @param id Category id.
     * @return `null` if deletion succeeds, otherwise an error message.
     */
    fun deleteCategory(id:String): String?{
        val category = cDao().findById(id) ?: return "There is no such a category!"
        cDao().delete(category)

        val todos = tDao().getAll()
        todos.forEach { todo ->
            if (todo.categoryIds.remove(id)) {
                tDao().update(todo)
            }
        }

        return null
    }

    /**
     * Finds a category by id.
     *
     * @param id Category id.
     * @return Matching [TodoCategory] or `null` if no category exists for this id.
     */
    fun findCategory(id:String): TodoCategory?{
        return cDao().findById(id)
    }

    /**
     * Updates name, color key and icon key of an existing category.
     *
     * Only changed values are updated.
     *
     * @param id Id of the category to edit.
     * @param newName New category name. Must not be blank.
     * @param newColorKey New optional color preset key.
     * @param newIconKey New optional icon preset key.
     * @return `null` if update succeeds, otherwise an error message.
     */
    fun editCategory(
        id: String,
        newName: String,
        newColorKey: String?,
        newIconKey: String?
    ): String? {

        val oldCategory: TodoCategory = findCategory(id) ?: return "There is no such a category!"
        if(newName.isBlank()) return "Title ca not be empty!"

        if(newName != oldCategory.name) oldCategory.name = newName
        if(newColorKey != oldCategory.colorKey) oldCategory.colorKey = newColorKey
        if(newIconKey != oldCategory.iconKey) oldCategory.iconKey = newIconKey
        cDao().update(oldCategory)

        return null
    }
}
