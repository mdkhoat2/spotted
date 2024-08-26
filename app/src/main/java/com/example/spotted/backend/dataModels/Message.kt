package com.example.spotted.backend.dataModels

import java.sql.Timestamp
import java.util.Date

data class SendMessageRequest(
    val receiverId: String,
    val content: String
)

data class Message(
    val sender: String,
    val receiver: String,
    val content: String,
    val sentAt: Timestamp,
    val readAt: Timestamp?
)