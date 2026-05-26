package com.example.mob_systeme2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mob_systeme2.model.Todo
import com.example.mob_systeme2.model.TodoCategory

/**
 * Room database that stores todos and categories.
 */
@Database(
    entities = [Todo::class, TodoCategory::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    /**
     * DAO for todo CRUD operations.
     */
    abstract fun todoDao(): TodoDao

    /**
     * DAO for category CRUD operations.
     */
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        /**
         * Returns singleton database instance for the app process.
         */
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "todo_room_db"
                ).allowMainThreadQueries().build().also { instance = it }
            }
        }
    }
}
