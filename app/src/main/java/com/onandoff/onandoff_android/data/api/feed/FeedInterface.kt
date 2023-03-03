package com.onandoff.onandoff_android.data.api.feed

import com.onandoff.onandoff_android.data.model.*
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
    ): FeedListResponse

    @GET("/feeds/feedlist/{profileId}")
    suspend fun getFeedListResult(
        @Path("profileId") profileId: Int,
        @Query("page") page: Int,
        @Query("categoryId") categoryId: Int,
        @Query("fResult") fResult: Boolean
    ): FeedListResponse

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
}