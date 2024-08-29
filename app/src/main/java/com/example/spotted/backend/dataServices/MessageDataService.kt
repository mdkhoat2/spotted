package com.example.spotted.backend.dataServices

import com.example.spotted.backend.dataModels.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
object MessageDataService {
    fun sendMessage(message: SendMessageRequest, onResult: (Message?) -> Unit) {
        DataService.apiService.sendMessage("Bearer ${DataService.authToken}", message).enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    DataService.extractMsg(response.errorBody())
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<Message>, t: Throwable) {
                onResult(null)
            }
        })
    }


    fun sendMessage(id: String, content: String, onResult: (Message?) -> Unit) {
        val message = SendMessageRequest(id, content)
        sendMessage(message, onResult)
    }
    fun getLastMessages(onResult: (List<Message>?) -> Unit){
        DataService.apiService.getLastMessages("Bearer ${DataService.authToken}").enqueue(object : Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    DataService.extractMsg(response.errorBody())
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun getMessages(id: String,onResult: (List<Message>?) -> Unit){
        DataService.apiService.getMessages(id,"Bearer ${DataService.authToken}").enqueue(object : Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    DataService.extractMsg(response.errorBody())
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                onResult(null)
            }
        })
    }
}