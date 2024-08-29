package com.example.spotted.backend.dataServices


import com.example.spotted.backend.dataModels.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AuthDataService {

    fun login(email: String, password: String, onResult: (LoginResponse?) -> Unit) {
        val request = LoginRequest(email, password)

        DataService.apiService.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                DataService.authToken = (response.body()?.token ?: "");
                DataService.authProfile =
                    (response.body()?.user ?: null)
                DataService.extractMsg(response.errorBody())
                if (response.isSuccessful) {
                    DataService.connect()
                }
                onResult(response.body())
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                DataService.msg = "Unknown error"
                onResult(null)
            }
        })
    }

    fun signUp(email: String, password: String, onResult: (SignUpResponse?) -> Unit) {
        val request = SignUpRequest(email, password)
        DataService.apiService.signUp(request).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                DataService.extractMsg(response.errorBody())
                onResult(response.body())
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                DataService.msg = "Unknown error"
                onResult(null)
            }
        })
    }

    private fun updateProfile(profile: ProfileUpdateRequest, onResult: (User?) -> Unit) {
        DataService.apiService.updateAuthProfile("Bearer ${DataService.authToken}", profile).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                DataService.authProfile = response.body()
                DataService.extractMsg(response.errorBody())
                onResult(response.body())
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                DataService.msg = "Unknown error"
                onResult(null)
            }
        })
    }
    fun updateProfile(name: String, age: Int, phone: String, description: String, interests: List<String>, onResult: (User?) -> Unit) {
        val profile = ProfileUpdateRequest(name, age, phone, description, interests)
        updateProfile(profile, onResult)
    }

    fun getProfile(onResult: (User?) -> Unit) {
        DataService.apiService.getAuthProfile("Bearer ${DataService.authToken}").enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                DataService.authProfile = response.body()
                DataService.extractMsg(response.errorBody())
                onResult(response.body())
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                DataService.msg = "Unknown error"
                onResult(null)
            }
        })
    }

    fun resetPassword(oldPassword: String, newPassword: String, onResult: (ResetPasswordResponse?) -> Unit) {
        val request = ResetPasswordRequest(oldPassword, newPassword)
        DataService.apiService.resetPassword("Bearer ${DataService.authToken}", request).enqueue(object : Callback<ResetPasswordResponse> {
            override fun onResponse(call: Call<ResetPasswordResponse>, response: Response<ResetPasswordResponse>) {
                DataService.extractMsg(response.errorBody())
                onResult(response.body())
            }

            override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                DataService.msg = "Unknown error"
                onResult(null)
            }
        })
    }

    fun forgotPassword(email: String, onResult: (ForgotPasswordResponse?) -> Unit) {
        val request = ForgotPasswordRequest(email)
        DataService.apiService.forgotPassword(request).enqueue(object : Callback<ForgotPasswordResponse> {
            override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                DataService.extractMsg(response.errorBody())
                onResult(response.body())
            }

            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                DataService.msg = "Unknown error"
                onResult(null)
            }
        })
    }
}
