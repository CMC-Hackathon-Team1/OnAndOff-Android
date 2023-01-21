package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName

data class UserSignUp(
    @SerializedName("email")val email:String,
    @SerializedName("password")val password:String
)
data class SignUpResponse(
    @SerializedName("statusCode")val statusCode:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result:SignUpResponseResult,
)
data class SignUpResponseResult(
    @SerializedName("userId")val userId:Int
)
