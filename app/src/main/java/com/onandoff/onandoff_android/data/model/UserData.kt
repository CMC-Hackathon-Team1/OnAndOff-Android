package com.onandoff.onandoff_android.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

//singIn, signUp 관련 data 클래스
@Keep
data class SignRequest(
    @SerializedName("email")val email:String,
    @SerializedName("password")val password:String
)

//signUp response data 클래스
@Keep
data class SignUpResponse(
    @SerializedName("statusCode")val statusCode:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result: SignUpResponseResult,
)

@Keep
data class SignUpResponseResult(
    @SerializedName("userId")val userId:Int
)

@Keep
data class SignInResponse(
    @SerializedName("statusCode")val statusCode:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result: SignInResponseResult,
)

@Keep
data class SignInResponseResult(
    @SerializedName("jwt")val jwt:String
)

@Keep
data class KakaoRequest(
    @SerializedName("access_token")val token:String
)

@Keep
data class GoogleRequest(
    @SerializedName("id_token")val token:String
)

@Keep
data class SocialLoginResponse(
    @SerializedName("isSuccess")val statusCode:String,
    @SerializedName("code")val message:Int,
    @SerializedName("result")val result: SocialLoginResponseResult,
)

@Keep
data class SocialLoginResponseResult(
    @SerializedName("state")val state:String,
    @SerializedName("jwt")val jwt:String
)
