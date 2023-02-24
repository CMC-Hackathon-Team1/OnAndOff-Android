package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName
import java.io.File


data class ProfileResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ProfileResponseResult
)

data class ProfileResponseResult(
    @SerializedName("profileId") val profileId: Int
)
data class ProfileListResponse(
    @SerializedName("statusCode")val statusCode :Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result:ArrayList<ProfileListResultResponse>
)
data class ProfileListResultResponse(
    @SerializedName("profileId")val profileId:Int,
    @SerializedName("personaName")val personaName:String,
    @SerializedName("profileName")val profileName: String,
    @SerializedName("profileImgUrl")val profileImgUrl: String,
    @SerializedName("statusMessage")val statusMessage: String,
    @SerializedName("createdAt")val createdAt: String,
)
