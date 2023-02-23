package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("email")
    val email: String?,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("status")
    val status: String?
)
