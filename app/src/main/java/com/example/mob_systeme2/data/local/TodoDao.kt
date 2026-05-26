package com.example.mob_systeme2.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mob_systeme2.model.Todo

/**
 * Room DAO for reading and writing todos.
 */
@Dao
interface TodoDao {
    /**
     * Returns all stored todos.
     */
    @Query("SELECT * FROM todos")
    fun getAll(): List<Todo>

    /**
     * Returns todo by id or null.
     */
    @Query("SELECT * FROM todos WHERE id = :id LIMIT 1")
    fun findById(id: String): Todo?

    /**
     * Inserts or replaces a todo.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(todo: Todo)

    /**
     * Updates a persisted todo row.
     */
    @Update
    fun update(todo: Todo)

    /**
     * Deletes a persisted todo row.
     */
    @Delete
    fun delete(todo: Todo)
}
