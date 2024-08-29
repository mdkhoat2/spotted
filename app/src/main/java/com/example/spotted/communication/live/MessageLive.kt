package com.example.spotted.communication.live

import com.example.spotted.backend.dataModels.Message

object MessageLive {
    private var onMessageReceivedCallback: ((Message) -> Unit)? = null

    // Method to set the callback
    fun setOnMessageReceivedCallback(callback: (Message) -> Unit) {
        onMessageReceivedCallback = callback
    }

    // This function will be called when a message is RECEIVED!
    fun onReceiveMessage(message: Message) {
        onMessageReceivedCallback?.invoke(message)
    }
}