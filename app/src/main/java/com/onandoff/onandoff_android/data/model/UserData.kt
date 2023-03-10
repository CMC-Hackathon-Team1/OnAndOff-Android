package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName

//singIn, signUp 관련 data 클래스
data class SignRequest(
    @SerializedName("email")val email:String,
    @SerializedName("password")val password:String
)
//signUp response data 클래스
data class SignUpResponse(
    @SerializedName("statusCode")val statusCode:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result: SignUpResponseResult,
)
data class SignUpResponseResult(
    @SerializedName("userId")val userId:Int
)
data class SignInResponse(
    @SerializedName("statusCode")val statusCode:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result: SignInResponseResult,
)
data class SignInResponseResult(
    @SerializedName("jwt")val jwt:String
)
data class KakaoRequest(
    @SerializedName("access_token")val token:String
)
data class GoogleRequest(
    @SerializedName("id_token")val token:String
)
data class SocialLoginResponse(
    @SerializedName("isSuccess")val statusCode:String,
    @SerializedName("code")val message:Int,
    @SerializedName("result")val result: SocialLoginResponseResult,
)
data class SocialLoginResponseResult(
    @SerializedName("state")val state:String,
    @SerializedName("jwt")val jwt:String
)

