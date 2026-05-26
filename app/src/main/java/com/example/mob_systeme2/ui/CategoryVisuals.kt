package com.example.mob_systeme2.ui

import android.graphics.Color

/**
 * Central mapping from persisted category keys to UI symbols and colors.
 */
object CategoryVisuals {
    fun iconFor(iconKey: String?): String = when (iconKey) {
        "book" -> "\uD83D\uDCDA"
        "work" -> "\uD83D\uDCBC"
        "home" -> "\uD83C\uDFE0"
        "sport" -> "\u26BD"
        "star" -> "\u2B50"
        else -> "\u2022"
    }

    fun colorFor(colorKey: String?): Int = when (colorKey) {
        "blue" -> Color.parseColor("#1E88E5")
        "green" -> Color.parseColor("#43A047")
        "orange" -> Color.parseColor("#FB8C00")
        "red" -> Color.parseColor("#E53935")
        "gray" -> Color.parseColor("#757575")
        else -> Color.parseColor("#607D8B")
    }
}
