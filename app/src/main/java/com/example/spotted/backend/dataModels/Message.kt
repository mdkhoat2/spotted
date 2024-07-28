package com.example.spotted.backend.dataModels

import java.sql.Timestamp

data class SendMessageRequest(
    val receiverId: String,
    val content: String
)

data class Message(
    val sender: String,
    val receiver: String,
    val content: String,
    val isRead: Boolean,
    val timestamp: Timestamp
)