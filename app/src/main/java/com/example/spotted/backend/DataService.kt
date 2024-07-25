package com.example.spotted.backend

import com.example.spotted.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DataService {
    internal val apiService: ApiService = ApiClient.createService(ApiService::class.java)
    internal var authToken: String = ""
    internal var authProfile: User? = null

    fun getLoggedUser(): User? {
        return authProfile
    }
}