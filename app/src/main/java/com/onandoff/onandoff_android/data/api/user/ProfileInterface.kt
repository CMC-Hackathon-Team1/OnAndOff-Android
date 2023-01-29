package com.onandoff.onandoff_android.data.api.user

import com.onandoff.onandoff_android.data.model.ProfileRequest
import com.onandoff.onandoff_android.data.model.ProfileResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ProfileInterface {
    @POST("/profiles/create")
    fun profileCreate(
        @Body body: ProfileRequest
    ): Call<ProfileResponse>
}