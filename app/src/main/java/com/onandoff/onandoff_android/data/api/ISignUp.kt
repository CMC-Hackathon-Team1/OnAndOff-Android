package com.onandoff.onandoff_android.data.api

import com.onandoff.onandoff_android.data.api.API.SIGNUP
import com.onandoff.onandoff_android.data.model.SignUpResponse
import com.onandoff.onandoff_android.data.model.UserSignUp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ISignUp {
    @POST(SIGNUP)
    fun signUp(@Body userSignUp: UserSignUp): Call<UserSignUp>
}