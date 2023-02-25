package com.onandoff.onandoff_android.data.api.user

import com.onandoff.onandoff_android.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProfileInterface {
    @Multipart
    @POST("/profiles")
    fun profileCreate(
        @Part profileName : MultipartBody.Part,
        @Part personaName: MultipartBody.Part,
        @Part statusMessage:  MultipartBody.Part,
        @Part image: MultipartBody.Part,
    ): Call<ProfileResponse>

    @GET("/profiles/{profileId}")
    fun getMyProfile(@Path("profileId") profileId:String):Call<getMyProfileResponse>

    @GET("/profiles/my-profiles")
    fun profileCheck(): Call<ProfileListResponse>
    @PATCH("/profiles/{profileId}")
    fun profileEidt(@Path("profileId")  profileId:String,
        @Body profileEditRequest: ProfileEditRequest
    ):Call<ProfileListResultResponse>

    @DELETE("/profiles/{profileId}")
    fun profileDelete(@Path("profileId") profileId:String):Call<ProfileResult>
}