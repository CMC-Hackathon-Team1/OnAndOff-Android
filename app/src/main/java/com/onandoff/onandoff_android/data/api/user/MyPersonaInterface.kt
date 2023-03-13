package com.onandoff.onandoff_android.data.api.user

import com.onandoff.onandoff_android.data.model.CreateMyProfileResponse
import com.onandoff.onandoff_android.data.model.MyProfileListResponse
import com.onandoff.onandoff_android.data.model.MyProfileResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface MyPersonaInterface {

    @Multipart
    @POST("/profiles")
    suspend fun createMyPersona(
        @Part profileName: MultipartBody.Part,
        @Part personaName: MultipartBody.Part,
        @Part statusMessage: MultipartBody.Part,
        @Part image: MultipartBody.Part
    ): CreateMyProfileResponse

    @Multipart
    @GET("/profiles/my-profiles/{}")
    suspend fun getMyPersona(
        @Path("profileId") profileId: Int
    ): MyProfileResponse

    @GET("/profiles/my-profiles")
    suspend fun getMyPersonaList(): MyProfileListResponse
}