package com.example.spotted.backend


import com.example.spotted.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthDataService {

    fun login(email: String, password: String, onResult: (LoginResponse?) -> Unit) {
        val request = LoginRequest(email, password)

        DataService.apiService.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                DataService.authToken = (response.body()?.token ?: "");
                DataService.authProfile = (response.body()?.user ?: User("", "", "", "", "", listOf(), false));
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
        DataService.apiService.signUp(request).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun updateProfile(profile: ProfileUpdateRequest, onResult: (User?) -> Unit) {
        DataService.apiService.updateAuthProfile("Bearer ${DataService.authToken}", profile).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                DataService.authProfile = response.body()
                onResult(response.body())
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                onResult(null)
            }
        })
    }
    fun updateProfile(name: String, age: Int, phone: String, description: String, interests: List<String>, onResult: (User?) -> Unit) {
        val profile = ProfileUpdateRequest(name, age, phone, description, interests)
        updateProfile(profile, onResult)
    }
}
