package com.onandoff.onandoff_android.data.api.feed

import com.onandoff.onandoff_android.data.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface FeedInterface {
    @Multipart
    @POST("/feeds")
    fun addFeedResponse(
        @Part profileId: MultipartBody.Part,
        @Part categoryId:  MultipartBody.Part,
        @Part hashTagList: List<MultipartBody.Part>,
        @Part images: MultipartBody.Part?=null,
        @Part content: MultipartBody.Part,
        @Part isSecret: MultipartBody.Part,
    ): Call<FeedResponse>

    @GET("/feeds/{feedId}/profiles/{profileId}")
    fun readFeedResponse(
        @Path("feedId") feedId: Int, @Path("profileId") profileId: Int
    ): Call<FeedReadData>

    @PATCH("/feeds")
    fun updateFeedResponse(
        @Body body: FeedData
    ): Call<FeedResponse>

    @PATCH("/feeds/status")
    fun deleteFeedResponse(
        @Body body:FeedSimpleData
    ): Call<FeedResponse>

    @POST("/likes")
    fun likeFeedResponse(
        @Body body:FeedSimpleData
    ): Call<FeedResponse>

    @GET("/categories/categories")
    fun getFeedCategoryResponse(): Call<CategoryResponse>
}