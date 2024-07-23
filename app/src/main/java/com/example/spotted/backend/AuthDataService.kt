package com.example.spotted.backend


import com.example.spotted.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthDataService {

    private val apiService: ApiService = ApiClient.createService(ApiService::class.java)

    fun login(email: String, password: String, onResult: (LoginResponse?) -> Unit) {
        val request = LoginRequest(email, password)

        apiService.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                println(t.message)
                onResult(null)
            }
        })
    }

    fun signUp(name: String, email: String, password: String, onResult: (SignUpResponse?) -> Unit) {
        val request = SignUpRequest(name, email, password)
        apiService.signUp(request).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }
}
