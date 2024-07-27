package com.example.spotted.backend.dataModels

data class LoginRequest(
    val email: String,
    val password: String
)


data class LoginResponse(
    val token: String,
    val user: User
)
