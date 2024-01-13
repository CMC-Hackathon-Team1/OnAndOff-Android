package com.onandoff.onandoff_android.data.api.user

import com.onandoff.onandoff_android.data.model.StatisticsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface StatisticsInterface {

    @GET("/statistics/{profileId}/monthly")
    suspend fun getMonthlyStatistics(
        @Path("profileId") profileId: Int
    ): StatisticsResponse

    // 공감 수 불러오기
    @GET("/statistics/{profileId}/likes/monthly")
    suspend fun getMonthlyLikesCount(
        @Path("profileId") profileId: Int
    ): StatisticsResponse

    // 게시글 수 불러오기
    @GET("/statistics/{profileId}/my-feeds/monthly")
    suspend fun getMonthlyMyFeedsCount(
        @Path("profileId") profileId: Int
    ): StatisticsResponse

    // 팔로워 수 불러오기
    @GET("/statistics/{profileId}/followers/monthly")
    suspend fun getMonthlyFollowersCount(
        @Path("profileId") profileId: Int
    ): StatisticsResponse
}
