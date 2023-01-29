package com.onandoff.onandoff_android.data.api.user

import com.onandoff.onandoff_android.data.model.ProfileRequest
import com.onandoff.onandoff_android.data.model.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProfileInterface {
    @Multipart
    @POST("/profiles/create")
    fun profileCreate(
//        @Part profileImgUrl : MultipartBody.Part,
        @Part userId:  MultipartBody.Part,
        @Part profileName : MultipartBody.Part,
        @Part personaName:  MultipartBody.Part,
        @Part statusMesage:  MultipartBody.Part,
    ): Call<ProfileResponse>
}