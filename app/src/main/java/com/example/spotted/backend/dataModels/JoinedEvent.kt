package com.example.spotted.backend.dataModels

import java.sql.Timestamp

data class Admin(
    val _id: String,
    val userID: String,
    val mode: String,
    val createdAt: Timestamp
)
data class JoinedEvent(
    val joinedAt: Timestamp,
    val event: Event,
    val admin: Admin
)
data class ResponseRequest(
    val action: String
)
data class ResultResponse(
    val msg: String
)

