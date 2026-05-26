package com.example.mob_systeme2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class TodoCategory(
    @PrimaryKey
    val id: String,
    var name: String,
    var colorKey: String?,
    var iconKey: String?
)
