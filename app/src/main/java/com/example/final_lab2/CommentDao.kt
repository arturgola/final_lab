package com.example.final_lab2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CommentDao {
    @Insert
    suspend fun insert(comment: Comment)

    @Query("SELECT * FROM comments WHERE ministerId = :ministerId")
    suspend fun getComments(ministerId: Int): List<Comment>
}
