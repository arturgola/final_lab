package com.example.final_lab2.viewmodel

import android.content.Context
import com.example.final_lab2.data.AppDatabase
import com.example.final_lab2.network.ApiService
import com.example.final_lab2.repository.CommentRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Date: 13.10.2024
// Name: Artur Golavskiy 2215446
// Description: The ViewModelProvider object provides a configured instance of MinisterViewModel with a Room database,
// a comment repository, and an API service for fetching ministers.

object ViewModelProvider {

    fun provideMinisterViewModel(context: Context): MinisterViewModel {
        val db = AppDatabase.getDatabase(context)
        val commentDao = db.commentDao()
        val repository = CommentRepository(commentDao)

        val apiService = Retrofit.Builder()
            .baseUrl("https://users.metropolia.fi/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val viewModel = MinisterViewModel(repository)

        viewModel.fetchMinisters(apiService)

        return viewModel
    }
}
