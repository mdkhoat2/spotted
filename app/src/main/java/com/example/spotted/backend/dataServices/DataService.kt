package com.example.spotted.backend.dataServices

import com.example.spotted.BuildConfig
import com.example.spotted.backend.api.ApiClient
import com.example.spotted.backend.api.ApiService
import com.example.spotted.backend.dataModels.ErrorResponse
import com.example.spotted.backend.dataModels.User
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




    fun connect() {
        try {
            mSocket = IO.socket(BuildConfig.API_BASE_URL)
            mSocket.connect()

            mSocket.on(Socket.EVENT_CONNECT, onConnect)
            mSocket.on("receiveMessage", onReceiveMessage)

            mSocket.emit("register", authProfile?.id)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    private val onConnect = Emitter.Listener {
        println("Connected")
    }

    private val onReceiveMessage = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        val message = data.getString("content")
        println("Received message: $message")
    }

    private fun sendMessage(sender: String, receiver: String, content: String) {
        val messageData = JSONObject()
        messageData.put("sender", sender)
        messageData.put("receiver", receiver)
        messageData.put("content", content)
        mSocket.emit("sendMessage", messageData)
    }

    fun disconnect() {
        mSocket.disconnect()
        mSocket.off(Socket.EVENT_CONNECT, onConnect)
        mSocket.off("receiveMessage", onReceiveMessage)
    }
}