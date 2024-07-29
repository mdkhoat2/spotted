package com.example.spotted.communication.adapter

import com.example.spotted.backend.dataModels.Message
import com.example.spotted.backend.dataServices.DataService

class MessageAdapter(message: Message) {
    private val message = message
    fun isReceived(): Boolean {
        if (DataService.getAuthProfile() == null)
            return false
        return message.receiver == DataService.getAuthProfile()?.id
    }
}