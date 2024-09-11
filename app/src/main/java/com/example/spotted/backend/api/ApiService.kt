package com.example.spotted.backend.api

import com.example.spotted.backend.dataModels.*
import okhttp3.MultipartBody

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("auth/signup")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    @POST("auth/reset-password")
    fun resetPassword(@Header("Authorization") token: String, @Body body: ResetPasswordRequest): Call<ResetPasswordResponse>

    @POST("auth/forgot-password")
    fun forgotPassword(@Body body: ForgotPasswordRequest): Call<ForgotPasswordResponse>

    // updateAuthProfile with Authorization header
    @PUT("auth/profile")
    fun updateAuthProfile(@Header("Authorization") token: String, @Body profile: ProfileUpdateRequest) : Call<User>

    @GET("auth/profile")
    fun getAuthProfile(@Header("Authorization") token: String) : Call<User>

    // Users
    @GET("users/{id}")
    fun getUser(@Path("id") id: String) : Call<User>

    // Messages
    @POST("messages/send")
    fun sendMessage(@Header("Authorization") token: String, @Body message: SendMessageRequest) : Call<Message>

    @GET("messages/last-messages")
    fun getLastMessages(@Header("Authorization") token: String) : Call<List<Message>>

    @GET("messages/inbox/{id}/{skip}")
    fun getMessages(
        @Path("id") id: String,
        @Path("skip") skip: Int,
        @Header("Authorization") token: String
    ): Call<List<Message>>

    //Events
    @POST("events/create")
    fun createEvent(@Header("Authorization") token: String, @Body event: Event) : Call<Event>

    @POST("events/invitation/accept/{id}")
    fun respondInvitation(@Header("Authorization") token: String, @Path("id") id: String,@Body Status: String): Call<Event>

    @GET("events/joined")
    fun getJoinedEvents(@Header("Authorization") token: String): Call<List<JoinedEvent>>

    @POST("events/search")
    fun getEvents(
        @Header("Authorization") token: String,
        @Body getEventsRequest: GetEventsRequest
    ): Call<List<Event>>

    @POST ("events/request-join")
    fun requestJoinEvent(@Header("Authorization") token: String,
                         @Body joinRequest: JoinRequest) : Call<Event>


    @POST("events/admin")
    fun inviteAdminEvents(
        @Header("Authorization") token: String,
        @Body eventId: String,
        @Body userId: String,
        @Body mode: String
    ): Call<String>

    @GET("events/get/{id}")
    fun getEventInfo(@Path("id") id: String, @Header("Authorization") token: String): Call<Event>

    @GET("events/get-role/{id}")
    fun getRole(@Path("id") id: String, @Header("Authorization") token: String): Call<String>

    @GET("events/get-participants/{id}")
    fun getParticipants(@Path("id") id: String, @Header("Authorization") token: String): Call<List<User>>

    @GET ("events/get-admins/{id}")
    fun getAdmins(@Path("id") id: String, @Header("Authorization") token: String): Call<List<Admin>>

    //Invite user to event
    @POST("events/invite")
    fun inviteUserToEvent(@Header("Authorization") token: String, @Body invite: InviteRequest) : Call<Invitation>

    // Update avatar
    @Multipart
    @PUT("auth/profile/avatar")
    fun updateAvatar(@Header("Authorization") token: String, @Part avatar: MultipartBody.Part) : Call<AvatarUpdateResponse>

    @GET("auth/profile/avatar/{id}")
    fun getAvatar(@Path("id") id: String) : Call<Avatar>

    @GET("notifications/get/{timestamp}")
    fun getNotifications(@Path("timestamp") timestamp: String, @Header("Authorization") token: String): Call<List<NotificationItem>>

    @POST("events/response/{id}")
    fun respondToRequest(@Header("Authorization") token: String, @Path("id") id: String, @Body response: ResponseRequest): Call<ResultResponse>
}