package com.onandoff.onandoff_android.data.api.feed


import com.onandoff.onandoff_android.data.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import com.onandoff.onandoff_android.data.request.FollowRequest
import com.onandoff.onandoff_android.data.request.LikeRequest
import com.onandoff.onandoff_android.data.request.ReportFeedRequest
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
    @POST("/follow")
    fun followResponse(
        @Body followRequest: FollowRequest
    ): Call<LikeFollowResponse>

    @POST("/follow/test")
    fun followStatusResponse(
        @Body followRequest: FollowRequest
    ): Call<LikeFollowResponse>

    @POST("/reports")
    suspend fun reportFeed(
        @Body reportFeedRequest: ReportFeedRequest
    ): ReportFeedResponse

    @GET("/categories/categories")
    suspend fun getCategories(): CategoryListResponse

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
    fun getFeedCategoryResponse(): Call<FeedCategoryResponse>

    @GET("/feeds/monthly")
    fun getOtherUserFeedListResponse(
        @Query("baseProfileId") baseProfileId:Int,
        @Query("targetProfileId") targetProfileId:Int,
        @Query("year")year:Int,
        @Query("month")month:String,
        @Query("page")page:Int
    ): Call<getFeedListRespone>
}