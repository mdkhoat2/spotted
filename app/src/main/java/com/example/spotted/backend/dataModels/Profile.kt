package com.example.spotted.backend.dataModels

import android.graphics.Bitmap
import okhttp3.MultipartBody
import java.io.File

data class ProfileUpdateRequest(
    val name: String,
    val age: Int,
    val phone: String,
    val description: String,
    val interests: String
)

data class AvatarUpdateRequest(
    val avatar: MultipartBody.Part
)

data class AvatarUpdateResponse(
    val msg: String,
    val url: String,
)
