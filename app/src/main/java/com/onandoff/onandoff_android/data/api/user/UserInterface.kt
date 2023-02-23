package com.onandoff.onandoff_android.data.api.user

import com.onandoff.onandoff_android.data.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface UserInterface {
    @POST("/auth/signup")
    fun signUp(
        @Body body:SignRequest
    ): Call<SignUpResponse>

    @POST("/auth/login")
    fun signIn(@Body userSignUp: SignRequest): Call<SignInResponse>
    @POST("/auth/kakao-login")
    fun kakaoLogIn(@Query(value="code",encoded = true) kakaoToken:String):Call<KakaoResponse>
//    @POST("/auth/logout")
//    @Header ({'Authorization', jwt:string})
//    fun logOut()Call<JsonElement>
}
