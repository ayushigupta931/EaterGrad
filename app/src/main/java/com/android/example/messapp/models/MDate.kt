package com.android.example.messapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "date")
data class mDate(
    @PrimaryKey var date: String = "",
    var breakfast: Boolean = false,
    var lunch: Boolean = false,
    var dinner: Boolean = false
)
