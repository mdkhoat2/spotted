package com.example.spotted.backend.dataModels

import java.sql.Timestamp

data class Event(
    // base on the backend data model
    val _id: String,
    val description: String,
    val start: Timestamp,
    val duration: Int,
    val location: List<Double>,
    val type: String,
    val joinMode: String,
    val maxParticipants: Int,
    val deadline: Timestamp
)

//    "description": "Badminton 3",
//    "start": "2024-08-30T00:00:00.000Z",
//    "duration": 60,
//    // hanoi location: 21.028511, 105.804817
//    "location": [21.028511, 105.804817],
//    "type": "Sport",
//    "joinMode": "Everyone",
//    "maxParticipants": "10",
//    "deadline": "2024-08-31T00:00:00.000Z"