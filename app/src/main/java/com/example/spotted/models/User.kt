package com.example.spotted.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val isVerified: Boolean
)

// LoginRequest.kt

data class LoginRequest(
    val email: String,
    val password: String
)

// LoginResponse.kt

data class LoginResponse(
    val token: String,
    val user: User
)

// SignUpRequest.kt

data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String
)

// SignUpResponse.kt

data class SignUpResponse(
    val message: String,
    val user: User
)