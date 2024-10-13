package com.example.final_lab2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.final_lab2.models.Comment

@Dao
interface CommentDao {
    @Insert
    suspend fun insert(comment: Comment)

    @Query("SELECT * FROM comments WHERE ministerId = :ministerId")
    suspend fun getComments(ministerId: Int): List<Comment>
}
