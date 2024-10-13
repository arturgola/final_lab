package com.example.final_lab2

data class Minister(
    val hetekaId: Int,           // Ensure this matches the JSON structure
    val seatNumber: Int,
    val lastname: String,
    val firstname: String,
    val party: String,
    val minister: Boolean,
    val pictureUrl: String
)