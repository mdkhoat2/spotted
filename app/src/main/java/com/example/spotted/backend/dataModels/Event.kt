package com.example.spotted.backend.dataModels

import java.sql.Timestamp

data class Event(
    val _id: String,
    val title: String,
    val description: String,
    val start: Timestamp,
    val duration: Int,
    val location: List<Double>,
    val type: String,
    val joinMode: String,
    val maxParticipants: Short,
    val deadline: Timestamp
)