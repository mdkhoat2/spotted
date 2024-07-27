package com.example.spotted.models

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val description: String,
    val interests: List<String>,
    val isVerified: Boolean
)

data class LoginRequest(
    val email: String,
    val password: String
)


data class LoginResponse(
    val token: String,
    val user: User
)


data class SignUpRequest(
    val email: String,
    val password: String
)

data class SignUpResponse(
    val message: String,
    val user: User
)

data class ProfileUpdateRequest(
    val name: String,
    val age: Int,
    val phone: String,
    val description: String,
    val interests: List<String>
)

data class ResetPasswordRequest(
    val oldPassword: String,
    val newPassword: String
)

data class ErrorResponse(
    val msg: String
)