package com.onandoff.onandoff_android.data.api.feed

import retrofit2.Call
import com.onandoff.onandoff_android.data.model.*
import com.onandoff.onandoff_android.data.request.FollowRequest
import com.onandoff.onandoff_android.data.request.LikeRequest
import com.onandoff.onandoff_android.data.request.ReportFeedRequest
import okhttp3.MultipartBody
import retrofit2.http.*

interface FeedInterface {
    @GET("/feeds/feedlist/{profileId}/search")
    suspend fun getSearchFeedResult(
        @Path("profileId") profileId: Int,
        @Query("page") page: Int,
        @Query("categoryId") categoryId: Int,
        @Query("fResult") fResult: Boolean,
        @Query("query") query: String?
    ): GetFeedListResponse

    @GET("/feeds/feedlist/{profileId}")
    suspend fun getFeedListResult(
        @Path("profileId") profileId: Int,
        @Query("page") page: Int,
        @Query("categoryId") categoryId: Int,
        @Query("fResult") fResult: Boolean
    ): GetFeedListResponse

    @GET("/feeds/{feedId}/profiles/{profileId}")
    suspend fun getFeedDetailResult(
        @Path("feedId") feedId: Int,
        @Path("profileId") profileId: Int
    ): FeedDetailResponse

    @POST("/likes")
    suspend fun like(
        @Body likeRequest: LikeRequest
    ): LikeFollowResponse

    @POST("/follow")
    suspend fun follow(
        @Body followRequest: FollowRequest
    ): LikeFollowResponse

    @POST("/feeds/report")
    suspend fun reportFeed(
        @Body reportFeedRequest: ReportFeedRequest
    ): ReportFeedResponse

    @GET("/categories/categories")
    suspend fun getCategories(): CategoryListResponse

    @Multipart
    @POST("/feeds")
    fun addFeedResponse(
        @Part profileId : MultipartBody.Part,
        @Part categoryId:  MultipartBody.Part,
        @Part hashTagList:  MultipartBody.Part,
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
        @Body body:FeedDeleteData
    ): Call<FeedResponse>
}