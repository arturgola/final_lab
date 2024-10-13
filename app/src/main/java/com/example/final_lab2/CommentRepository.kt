package com.example.final_lab2

class CommentRepository(private val commentDao: CommentDao) {
    suspend fun insertComment(comment: Comment) {
        commentDao.insert(comment)
    }

    suspend fun getComments(ministerId: Int): List<Comment> {
        return commentDao.getComments(ministerId)
    }
}
