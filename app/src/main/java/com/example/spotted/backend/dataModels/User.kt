package com.example.spotted.backend.dataModels

data class User(
    val _id: String,
    val name: String,
    val email: String,
    val phone: String,
    val description: String,
    val interests: List<String>,
    val isVerified: Boolean
)

data class ErrorResponse(
    val msg: String
)