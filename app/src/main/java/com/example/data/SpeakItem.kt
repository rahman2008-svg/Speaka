package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "speak_items")
data class SpeakItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val title: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val languageCode: String = "en",
    val languageName: String = "English",
    val pitch: Float = 1.0f,
    val rate: Float = 1.0f
) {
    val displayTitle: String
        get() = if (title.isNotBlank()) title else {
            val words = text.split("\\s+".toRegex()).take(5)
            val joined = words.joinToString(" ")
            if (joined.length < text.length) "$joined..." else joined
        }
}
