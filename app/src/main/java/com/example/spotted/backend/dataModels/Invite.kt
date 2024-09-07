package com.example.spotted.backend.dataModels

import java.sql.Timestamp

data class InviteRequest(
    val eventID: String,
    val userID: String
)

data class JoinRequest(
    val eventID: String,
    val userIDs: List<String>
)

data class Invitation(
    val userID: String,
    val adminID: String,
    val expiresIn: Long,
    val _id: String,
    val invitedAt: Timestamp
)