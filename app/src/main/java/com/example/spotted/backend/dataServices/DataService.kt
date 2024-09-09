package com.example.spotted.backend.dataServices

import com.example.spotted.backend.api.ApiClient
import com.example.spotted.backend.api.ApiService
import com.example.spotted.backend.dataModels.ErrorResponse
import com.example.spotted.backend.dataModels.*
import com.example.spotted.communication.live.MessageLive
import com.example.spotted.communication.live.NotificationLive
import com.google.gson.Gson
import io.socket.client.Socket
import okhttp3.ResponseBody
import io.socket.client.IO
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.net.URISyntaxException

object DataService {
    internal val apiService: ApiService = ApiClient.createService(ApiService::class.java)
    internal var authToken: String = ""
    internal var authProfile: User? = null
    internal var msg: String = ""
    private lateinit var mSocket: Socket

    fun getAuthProfile(): User? {
        return authProfile
    }

    fun extractMsg(errorBody: ResponseBody?): String {
        if (errorBody == null) {
            msg = "Unknown error"
            return "Unknown error"
        }
        val gson = Gson()
        val errorResponse = gson.fromJson(errorBody?.charStream(), ErrorResponse::class.java)
        msg = errorResponse?.msg ?: "Unknown error"
        return msg
    }

    fun getMsg(): String {
        return msg
    }

    internal fun connect() {
        try {
            mSocket = IO.socket(ApiClient.BASE_URL)
            mSocket.connect()
            mSocket.on(Socket.EVENT_CONNECT, onConnect)
            mSocket.on("receiveMessage", onReceiveMessage)
            mSocket.on("receiveNotification", onRecieveNotification)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    private val onConnect = Emitter.Listener {
        println("Server connected")
        mSocket.emit("register", authProfile?._id)
    }

    private val onReceiveMessage = Emitter.Listener { args ->
        val data = args[0] as JSONObject

        // extract to Message
        val messageData = Gson().fromJson(data.toString(), Message::class.java)
        MessageLive.onReceiveMessage(messageData);
    }

    private val onRecieveNotification = Emitter.Listener { args ->
        val data = args[0] as JSONObject

        // extract to Notification
        val notificationData = Gson().fromJson(data.toString(), NotificationItem::class.java)
         NotificationLive.onReceiveNotification(notificationData);
    }

    internal fun disconnect() {
        mSocket.disconnect()
        mSocket.off(Socket.EVENT_CONNECT, onConnect)
        mSocket.off("receiveMessage", onReceiveMessage)
    }
}