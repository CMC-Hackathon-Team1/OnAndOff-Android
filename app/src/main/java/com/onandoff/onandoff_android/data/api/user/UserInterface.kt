package com.onandoff.onandoff_android.data.api.user

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.onandoff.onandoff_android.data.model.*
import retrofit2.Call
import retrofit2.http.*

interface UserInterface {
    @POST("/auth/signup/{level}")
    fun signUp(
        @Body body:SignRequest,
        @Path("level") level:Int
    ): Call<SignUpResponse>

    @POST("/auth/login")
    fun signIn(@Body userSignUp: SignRequest): Call<SignInResponse>
    @POST("/auth/kakao-login")
    fun kakaoLogIn(@Body token:KakaoRequest ):Call<SocialLoginResponse>


    @POST("/auth/google-login")
    fun googleLogIn(@Body token:GoogleRequest):Call<SocialLoginResponse>

    @FormUrlEncoded
    @POST("/users/send-mail")
    fun sendFeedBack(@Field("content")content:String):Call<ProfileResult>

    @DELETE("/users/account")
    fun exitAccount():Call<ProfileResult>

    @POST("/auth/logout")
    fun logout():Call<JsonElement>

    @GET("/users/email")
    fun getEmail():Call<getMyEmail>


}
