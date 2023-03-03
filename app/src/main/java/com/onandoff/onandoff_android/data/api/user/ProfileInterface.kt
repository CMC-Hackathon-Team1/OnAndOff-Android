package com.onandoff.onandoff_android.data.api.user

import com.google.gson.annotations.SerializedName
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
        @Part image: MultipartBody.Part?=null,
    ): Call<ProfileResponse>

    @GET("/profiles/{profileId}")
    fun getMyProfile(@Path("profileId") profileId:Int):Call<getMyProfileResponse>

    @GET("/profiles/my-profiles")
    fun profileCheck(): Call<ProfileListResponse>

    @Multipart
    @PATCH("/profiles/{profileId}")
    fun profileEdit(@Path("profileId")  profileId:Int,
        @Part profileName: MultipartBody.Part,
        @Part statusMessage: MultipartBody.Part,
        @Part image: MultipartBody.Part?=null,
        @Part defaultImage: MultipartBody.Part,
    ):Call<ProfileListResultResponse>

    @DELETE("/profiles/{profileId}")
    fun profileDelete(@Path("profileId") profileId:Int):Call<ProfileResult>
}