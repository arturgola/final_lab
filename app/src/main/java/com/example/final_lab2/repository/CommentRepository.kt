package com.example.final_lab2.repository

import com.example.final_lab2.data.CommentDao
import com.example.final_lab2.models.Comment

// Date: 13.10.2024
// Name: Artur Golavskiy 2215446
// Description: This file defines the CommentRepository class for managing comments in the database.

class CommentRepository(private val commentDao: CommentDao) {
    suspend fun insertComment(comment: Comment) {
        commentDao.insert(comment)
    }

    suspend fun getComments(ministerId: Int): List<Comment> {
        return commentDao.getComments(ministerId)
    }
}
