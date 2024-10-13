package com.example.final_lab2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // Primary key
    val ministerId: Int,  // Foreign key to link comments to ministers
    val rating: Int,
    val comment: String
)
