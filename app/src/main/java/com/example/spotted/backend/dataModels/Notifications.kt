package com.example.spotted.backend.dataModels

import java.sql.Timestamp

data class NotificationModel(
    val type: String,
    val message: String,
)