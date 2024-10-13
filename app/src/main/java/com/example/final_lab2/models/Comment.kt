package com.example.final_lab2.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ministerId: Int,
    val rating: Int,
    val comment: String
)
