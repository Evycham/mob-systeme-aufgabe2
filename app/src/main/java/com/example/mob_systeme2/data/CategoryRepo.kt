package com.example.mob_systeme2.data

import com.example.mob_systeme2.model.TodoCategory
import java.util.UUID

object CategoryRepo {

    val categoryList = mutableListOf<TodoCategory>()


    fun createCategory(
        id: String,
        name: String,
        colorKey: String?,
        iconKey: String?
    ): String?{

        if(name.isBlank()) return "Name can not be empty!"

        val id = UUID.randomUUID().toString()
        val category = TodoCategory(id, name, colorKey, iconKey)

        categoryList.add(category)

        return null
    }

    fun deleteCategory(id:String): String?{
        val removed = categoryList.removeIf { it.id == id }

        return if(removed) null else "There is no such a category!"
    }

    fun findCategory(id:String): TodoCategory?{
        return categoryList.find { it.id == id }
    }

    fun editCategory(
        id: String,
        newName: String,
        newColorKey: String?,
        newIconKey: String?
    ): String? {

        val oldCategory: TodoCategory = findCategory(id) ?: return "There is no such a category!"
        if(newName.isBlank()) return "Title ca not be empty!"

        if(newName != oldCategory.name) oldCategory.name = newName

        return null
    }


}