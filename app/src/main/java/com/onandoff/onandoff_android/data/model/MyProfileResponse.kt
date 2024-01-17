package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName

data class MyProfileResponse(
    @SerializedName("profileId")
    val profileId: Int,
    @SerializedName("personaName")
    val personaName: String?,
    @SerializedName("profileName")
    val profileName: String?,
    @SerializedName("statusMessage")
    val statusMessage: String?,
    @SerializedName("profileImgUrl")
    val profileImgUrl: String?,
    @SerializedName("createdAt")
    val createdAt: String?
)
