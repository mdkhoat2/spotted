package com.example.spotted.communication.adapters

import com.example.spotted.backend.dataModels.Message
import com.example.spotted.backend.dataServices.DataService

class MessageAdapter(message: Message) {
    private val message = message
    fun isReceived(): Boolean {
        if (DataService.getAuthProfile() == null)
            return false
        return message.receiver == DataService.getAuthProfile()?._id
    }
    fun getOtherUserId(): String {
        if (DataService.getAuthProfile() == null)
            return ""
        return if (isReceived()) message.sender else message.receiver
    }

    fun isRead(): Boolean {
        return message.readAt != null
    }
}