package com.example.spotted.communication.live

import com.example.spotted.backend.dataModels.NotificationItem

object NotificationLive {
    fun onReceiveNotification(notification: NotificationItem) {
        println("Notification received")
    }
}