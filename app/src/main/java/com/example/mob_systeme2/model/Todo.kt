package com.example.mob_systeme2.model

import java.time.LocalDate

data class Todo (
    val id: String,
    var title: String,
    var description: String,
    var priority: Int,
    var category: String,
    var done: Boolean,
    var dueDate: LocalDate?
)