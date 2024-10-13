package com.example.final_lab2.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_lab2.network.ApiService
import com.example.final_lab2.models.Comment
import com.example.final_lab2.repository.CommentRepository
import com.example.final_lab2.models.Minister
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Date: 13.10.2024
// Name: Artur Golavskiy 2215446
// Description: MinisterViewModel class, which handles fetching and managing ministers and their comments.

// The MinisterViewModel class manages the state and logic for fetching and storing minister data and comments.
class MinisterViewModel(private val repository: CommentRepository) : ViewModel() {

    private val _ministers = MutableStateFlow<List<Minister>>(emptyList())
    val ministers = _ministers.asStateFlow()

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments = _comments.asStateFlow()

    private val TAG = "MinisterViewModel"

    // Fetches a list of ministers from the API and updates the state.
    fun fetchMinisters(apiService: ApiService) {
        Log.d(TAG, "Fetching ministers from API...")
        apiService.getMinisters().enqueue(object : Callback<List<Minister>> {
            override fun onResponse(call: Call<List<Minister>>, response: Response<List<Minister>>) {
                if (response.isSuccessful) {
                    response.body()?.let { fetchedMinisters ->
                        val filteredMinisters = fetchedMinisters.filter { minister -> minister.minister }
                        _ministers.value = filteredMinisters
                        Log.d(TAG, "Fetched ${filteredMinisters.size} ministers.")
                    } ?: run {
                        Log.d(TAG, "Response body is null.")
                    }
                } else {
                    Log.e(TAG, "Error fetching ministers: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Minister>>, t: Throwable) {
                Log.e(TAG, "Failed to fetch ministers: ${t.message}", t)
            }
        })
    }

    // Inserts a new comment and fetches updated comments for the given minister.
    fun addComment(ministerId: Int, rating: Int, comment: String) {
        viewModelScope.launch {
            repository.insertComment(Comment(ministerId = ministerId, rating = rating, comment = comment))
            fetchComments(ministerId)
        }
    }

    // Fetches comments for a specific minister and updates the state.
    fun fetchComments(ministerId: Int) {
        viewModelScope.launch {
            val fetchedComments = repository.getComments(ministerId)
            _comments.value = fetchedComments
        }
    }
}