package com.example.spotted.backend

import com.example.spotted.models.*

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("auth/signup")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>
}