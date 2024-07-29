package com.example.spotted.backend.dataModels

data class ResetPasswordRequest(
    val oldPassword: String,
    val newPassword: String
)

data class ResetPasswordResponse(
    val msg: String
)

data class ForgotPasswordRequest(
    val email: String
)

data class ForgotPasswordResponse(
    val msg: String
)