package com.example.spotted.backend.dataModels

data class ProfileUpdateRequest(
    val name: String,
    val age: Int,
    val phone: String,
    val description: String,
    val interests: String
)

