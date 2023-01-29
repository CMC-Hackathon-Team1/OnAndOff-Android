package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName

data class ProfileRequest(
    @SerializedName("userId")val userId:Int,
    @SerializedName("profileName")val profileName: String,
    @SerializedName("personaName")val personaName:String,
    @SerializedName("profileImgUrl")val profileImgUrl: String,
    @SerializedName("statusMesage")val statusMesage: String,
)
data class ProfileResponse(
    @SerializedName("statusCode")val statusCode :Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result:ProfileResponseResult
)
data class ProfileResponseResult(
    @SerializedName("profileId")val profileId:Int
)
