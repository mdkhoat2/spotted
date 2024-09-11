package com.example.spotted.backend.dataModels

import okhttp3.Address
import java.sql.Timestamp

data class Event(
    // base on the backend data model
    val _id: String,
    val title: String,
    val description: String?,
    val start: Timestamp,
    val duration: Int,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val type: String,
    val joinMode: String,
    val maxParticipants: Int,
    val deadline: Timestamp?
)

data class GetEventsRequest(
    val latitude: Double,
    val longitude: Double,
    val distance: Double
)

data class RequestToJoin(
    val request: RequestID,
    val users:List<User>
)

data class RequestID(
    val _id: String,
    val eventID: String,
    val status: String
)