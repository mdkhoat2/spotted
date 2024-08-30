package com.example.spotted.backend.dataModels

import java.sql.Timestamp

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val start: Timestamp,
    val duration: Int,
    val location: List<Double>,
    val type: String,
    val joinMode: String,
    val maxParticipants: Int,
    val deadline: Timestamp
) {
    fun isOver(): Boolean {
        return start.time + duration * 60 * 1000 < System.currentTimeMillis()
    }
}