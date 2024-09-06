package com.example.spotted.backend.dataModels

import java.sql.Timestamp


data class NotificationItem(
    val type: String = "",
    var typeInt: Int = 1,
    val message: String = "Hello world",
    val event: Event? = null
)

data class RequestData(
    val event: Event,
    val userName: String
)