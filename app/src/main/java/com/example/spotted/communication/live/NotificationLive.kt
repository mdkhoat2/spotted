package com.example.spotted.communication.live

import NotificationAdapter
import com.example.spotted.backend.dataModels.Message
import com.example.spotted.backend.dataModels.NotificationItem
import com.example.spotted.util.SupportUtil

object NotificationLive {
    private var onNotificationReceivedCallback: ((NotificationItem) -> Unit)? = null
    private var onUpdateBadgeCallback: (() -> Unit)? = null
    private var numberUnreadNotification = 0

    private const val ENTER_NOTIFICATION_FRAGMENT = 1
    private const val OUT_OF_NOTIFICATION_FRAGMENT = 2
    private const val OUT_OF_MAIN_ACTIVITY = 3

    private var status = OUT_OF_NOTIFICATION_FRAGMENT

    // Method to set the callback
    fun setOnNotificationReceivedCallback(callback: (NotificationItem) -> Unit) {
        onNotificationReceivedCallback = callback
    }

    fun setOnUpdateBadgeCallback(callback: () -> Unit) {
        onUpdateBadgeCallback = callback
    }

    fun isHasNotificationCallback(): Boolean {
        return onNotificationReceivedCallback != null
    }

    fun isHasUpdateBadgeCallback(): Boolean {
        return onUpdateBadgeCallback != null
    }

    fun onReceiveNotification(notification: NotificationItem) {
        if (SupportUtil.getNotificationType(notification.type) == NotificationAdapter.NONE)
            return
        println("Status " + status.toString())
        when(status){
            ENTER_NOTIFICATION_FRAGMENT -> {
                onNotificationReceivedCallback?.invoke(notification)
            }
            OUT_OF_NOTIFICATION_FRAGMENT -> {
                numberUnreadNotification++
                onUpdateBadgeCallback?.invoke()
            }
            else -> {
                numberUnreadNotification++
            }
        }
    }

    fun getNumberUnreadNotification(): Int {
        return numberUnreadNotification
    }

    fun statusEnterNotificationFragment(){
        status = ENTER_NOTIFICATION_FRAGMENT
        numberUnreadNotification = 0
    }

    fun statusOutOfNotificationFragment(){
        status = OUT_OF_NOTIFICATION_FRAGMENT
    }

    fun statusOutOfMainActivity(){
        status = OUT_OF_MAIN_ACTIVITY
    }

    fun resetNotification(){
        numberUnreadNotification = 0
        status = OUT_OF_MAIN_ACTIVITY
        setOnUpdateBadgeCallback {}
        setOnNotificationReceivedCallback {}
    }
}