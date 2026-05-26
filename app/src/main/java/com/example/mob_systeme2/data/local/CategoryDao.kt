package com.example.mob_systeme2.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mob_systeme2.model.TodoCategory

/**
 * Room DAO for reading and writing categories.
 */
@Dao
interface CategoryDao {
    /**
     * Returns all stored categories.
     */
    @Query("SELECT * FROM categories")
    fun getAll(): List<TodoCategory>

    /**
     * Returns category by id or null.
     */
    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    fun findById(id: String): TodoCategory?

    /**
     * Inserts or replaces a category.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: TodoCategory)

    /**
     * Updates a persisted category row.
     */
    @Update
    fun update(category: TodoCategory)

    /**
     * Deletes a persisted category row.
     */
    @Delete
    fun delete(category: TodoCategory)
}
