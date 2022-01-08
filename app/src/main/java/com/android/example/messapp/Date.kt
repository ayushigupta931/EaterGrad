package com.android.example.messapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "date")
data class Date(
    @PrimaryKey val date: String = "",
    val breakfast: Boolean = false,
    val lunch: Boolean = false,
    val dinner: Boolean = false
)
