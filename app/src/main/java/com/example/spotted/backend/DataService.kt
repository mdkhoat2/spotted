package com.example.spotted.backend

import com.example.spotted.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DataService {
    val apiService: ApiService = ApiClient.createService(ApiService::class.java)
    var authToken: String = ""
    var authProfile: User? = null
}