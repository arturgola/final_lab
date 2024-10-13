package com.example.final_lab2

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("~peterh/seating.json")
    fun getMinisters(): Call<List<Minister>>
}