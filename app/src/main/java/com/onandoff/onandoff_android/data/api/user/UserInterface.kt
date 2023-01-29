package com.onandoff.onandoff_android.data.api.user

import com.google.gson.JsonElement
import com.onandoff.onandoff_android.data.model.SignRequest
import com.onandoff.onandoff_android.data.model.SignResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST

interface UserInterface {
    @POST("/auth/signup")
    fun signUp(
        @Body body:SignRequest
    ): Call<SignResponse>

    @POST("/auth/login")
    fun signIn(@Body userSignUp: SignRequest): Call<SignResponse>

//    @POST("/auth/logout")
//    @Header ({'Authorization', jwt:string})
//    fun logOut()Call<JsonElement>
}
