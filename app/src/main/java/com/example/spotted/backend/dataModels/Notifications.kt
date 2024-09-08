package com.example.spotted.backend.dataModels

import java.sql.Timestamp


data class NotificationItem(
    val type: String = "",
    var typeInt: Int = 1,
    val content: String = "Hello world",
    val eventID: String = ""
)

data class RequestData(
    val event: Event,
    val userName: String
)