package com.example.final_lab2.network

import com.example.final_lab2.models.Minister
import retrofit2.Call
import retrofit2.http.GET

// Date: 13.10.2024
// Name: Artur Golavskiy 2215446
// Description: Fetching minister data from a remote API.

interface ApiService {
    @GET("~peterh/seating.json")
    fun getMinisters(): Call<List<Minister>>
}