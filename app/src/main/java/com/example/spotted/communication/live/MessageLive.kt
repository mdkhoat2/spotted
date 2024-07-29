package com.example.spotted.communication.live

import com.example.spotted.backend.dataModels.Message

object MessageLive {
    fun onReceiveMessage(message: Message) {
        // For frontend members:
        // This function will be called when a message is RECEIVED!
        println(message)
    }
}