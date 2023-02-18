package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName

data class StatisticsRequest(
    val monthly_likes_count: Int,
    val monthly_myFeeds_count: Int,
    val monthly_myFollowers_count: Int
)
data class StatisticsResponse(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String?,
    @SerializedName("result")
    val result: StatisticsResponseResult
)

data class StatisticsResponseResult(
    @SerializedName("monthly_likes_count")
    val monthlyLikesCount: Int,
    @SerializedName("monthly_myFeeds_count")
    val monthlyMyFeedsCount: Int,
    @SerializedName("monthly_myFollowers_count")
    val monthlyMyFollowersCount: Int
)