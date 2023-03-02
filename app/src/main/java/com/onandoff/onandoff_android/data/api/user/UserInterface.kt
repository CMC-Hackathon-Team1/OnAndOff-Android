package com.onandoff.onandoff_android.data.api.user

import com.google.gson.annotations.SerializedName
import com.onandoff.onandoff_android.data.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserInterface {
    @POST("/auth/signup/{level}")
    fun signUp(
        @Body body:SignRequest,
        @Path("level") level:Int
    ): Call<SignUpResponse>

    @POST("/auth/login")
    fun signIn(@Body userSignUp: SignRequest): Call<SignInResponse>
    @POST("/auth/kakao-login")
    fun kakaoLogIn(@Body token:KakaoRequest ):Call<KakaoResponse>
//    @POST("/auth/logout")
//    @Header ({'Authorization', jwt:string})
//    fun logOut()Call<JsonElement>
}
