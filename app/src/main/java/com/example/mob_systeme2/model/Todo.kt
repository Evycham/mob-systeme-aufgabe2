package com.example.mob_systeme2.model

data class Todo (
    val id: Int,
    var title: String,
    var description: String,
    var priority: Int,
    var category: String,
    var done: Boolean,
    var dueDate: String
)