package com.example.mob_systeme2.data.local

import androidx.room.TypeConverter
import java.time.LocalDate

/**
 * Type converters used by Room for non-primitive fields.
 */
class Converters {
    /**
     * Converts LocalDate to ISO string for persistence.
     */
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.toString()

    /**
     * Converts ISO string back to LocalDate.
     */
    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let(LocalDate::parse)

    /**
     * Converts category id set to comma-separated string.
     */
    @TypeConverter
    fun fromCategoryIds(value: MutableSet<String>): String = value.joinToString(",")

    /**
     * Converts comma-separated category id string back to a mutable set.
     */
    @TypeConverter
    fun toCategoryIds(value: String): MutableSet<String> {
        if (value.isBlank()) return mutableSetOf()
        return value.split(",").toMutableSet()
    }
}
