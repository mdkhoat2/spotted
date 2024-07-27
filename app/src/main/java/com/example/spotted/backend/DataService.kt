package com.example.spotted.backend

import com.example.spotted.models.*
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DataService {
    internal val apiService: ApiService = ApiClient.createService(ApiService::class.java)
    internal var authToken: String = ""
    internal var authProfile: User? = null
    internal var msg: String = ""

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
}