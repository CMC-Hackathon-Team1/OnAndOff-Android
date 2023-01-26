package com.onandoff.onandoff_android.data.api.user

import com.google.gson.JsonElement
import com.onandoff.onandoff_android.data.model.SignRequest
import com.onandoff.onandoff_android.data.model.SignUpResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST

interface UserInterface {
    @POST("/auth/signup")
    fun signUp(
        @Body body:SignRequest
    ): Call<SignUpResponse>

    @POST("/auth/login")
    fun logIn(@Body userSignUp: SignRequest): Call<JsonElement>

//    @POST("/auth/logout")
//    @Header ({'Authorization', jwt:string})
//    fun logOut()Call<JsonElement>
}
