package com.example.spotted.backend.dataModels

import java.sql.Timestamp

data class Admin(
    val _id: String,
    val eventID: String,
    val userID: String,
    val mode: String,
    val createdAt: Timestamp,
    val __v: Int
)
data class JoinedEvent(
    val joinedAt: Timestamp,
    val event: Event,
    val admin: Admin
)
