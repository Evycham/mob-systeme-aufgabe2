package com.example.mob_systeme2.model

import java.time.LocalDate

/**
 * Data model representing a single todo entry.
 *
 * @property id unique identifier of the todo
 * @property title short visible title
 * @property description optional longer explanation
 * @property priority priority value from 1 to 3
 * @property category free-text category of the todo
 * @property done completion flag
 * @property dueDate optional deadline
 */
data class Todo (
    val id: String,
    var title: String,
    var description: String,
    var priority: Int,
    var category: String,
    var done: Boolean,
    var dueDate: LocalDate?
)
