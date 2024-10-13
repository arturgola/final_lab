package com.example.final_lab2

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MinisterViewModel(private val repository: CommentRepository) : ViewModel() {

    // StateFlow to hold the list of ministers
    private val _ministers = MutableStateFlow<List<Minister>>(emptyList())
    val ministers = _ministers.asStateFlow() // Expose as StateFlow for UI

    private val TAG = "MinisterViewModel" // Tag for logging

    fun fetchMinisters(apiService: ApiService) {
        Log.d(TAG, "Fetching ministers from API...") // Log the start of the fetch
        apiService.getMinisters().enqueue(object : Callback<List<Minister>> {
            override fun onResponse(call: Call<List<Minister>>, response: Response<List<Minister>>) {
                if (response.isSuccessful) {
                    response.body()?.let { fetchedMinisters ->
                        // Filter and update StateFlow
                        val filteredMinisters = fetchedMinisters.filter { minister -> minister.minister }
                        _ministers.value = filteredMinisters // Update StateFlow
                        Log.d(TAG, "Fetched ${filteredMinisters.size} ministers.") // Log the number of ministers fetched
                    } ?: run {
                        Log.d(TAG, "Response body is null.") // Log if the response body is null
                    }
                } else {
                    Log.e(TAG, "Error fetching ministers: ${response.code()} ${response.message()}") // Log error response
                }
            }

            override fun onFailure(call: Call<List<Minister>>, t: Throwable) {
                Log.e(TAG, "Failed to fetch ministers: ${t.message}", t) // Log the failure message
            }
        })
    }

    fun addComment(ministerId: Int, rating: Int, comment: String) {
        viewModelScope.launch {
            repository.insertComment(Comment(ministerId = ministerId, rating = rating, comment = comment))
        }
    }

    suspend fun getComments(ministerId: Int): List<Comment> {
        return repository.getComments(ministerId)
    }
}