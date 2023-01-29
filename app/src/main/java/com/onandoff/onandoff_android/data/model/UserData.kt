package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName

//singIn, signUp 관련 data 클래스
data class SignRequest(
    @SerializedName("email")val email:String,
    @SerializedName("password")val password:String
)
//signUp response data 클래스
data class SignResponse(
    @SerializedName("statusCode")val statusCode:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result: SignResponseResult,
)
data class SignResponseResult(
    @SerializedName("userId")val userId:Int
)

