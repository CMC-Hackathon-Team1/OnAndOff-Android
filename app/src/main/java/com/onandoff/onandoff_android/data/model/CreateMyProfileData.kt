package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName
import java.io.File

data class CreateMyProfileData(
    @SerializedName("profileName")
    val profileName: String,
    @SerializedName("personaName")
    val personaName: String,
    @SerializedName("statusMessage")
    val statusMessage: String,
    @SerializedName("profileImgUrl")
    val image: File?
)

data class CreateMyProfileResponse(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: CreateMyProfileResponseResult
)

data class CreateMyProfileResponseResult(
    @SerializedName("profileId")
    val profileId: Int
)
