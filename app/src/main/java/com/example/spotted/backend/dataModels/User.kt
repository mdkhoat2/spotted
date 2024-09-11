package com.example.spotted.backend.dataModels

data class User(
    val _id: String,
    val name: String,
    val email: String,
    val phone: String,
    val description: String,
    val interests: String,
    val isVerified: Boolean,
    val age: Int
)

data class Avatar(
    val url: String
)

data class ErrorResponse(
    val msg: String
)