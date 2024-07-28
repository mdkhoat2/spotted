package com.example.spotted.backend.dataModels


data class SignUpRequest(
    val email: String,
    val password: String
)

data class SignUpResponse(
    val message: String,
    val user: User
)