package com.example.spotted.backend.dataServices

import com.example.spotted.backend.dataModels.NotificationItem
import com.example.spotted.backend.dataModels.RequestData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object NotificationDataService {
    fun getNotifications(timestamp: String, onResult: (List<NotificationItem>?) -> Unit) {
        DataService.apiService.getNotifications(timestamp, "Bearer ${DataService.authToken}").enqueue(object : Callback<List<NotificationItem>> {
            override fun onResponse(call: Call<List<NotificationItem>>, response: Response<List<NotificationItem>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    DataService.extractMsg(response.errorBody())
                    onResult(null)
                }
            }
            override fun onFailure(call: Call<List<NotificationItem>>, t: Throwable) {
                onResult(null)
            }
        })
    }
}