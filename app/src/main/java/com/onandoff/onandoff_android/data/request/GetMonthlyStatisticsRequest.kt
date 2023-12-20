package com.onandoff.onandoff_android.data.request

data class GetMonthlyStatisticsRequest(
    val monthly_likes_count: Int,
    val monthly_myFeeds_count: Int,
    val monthly_myFollowers_count: Int
)
