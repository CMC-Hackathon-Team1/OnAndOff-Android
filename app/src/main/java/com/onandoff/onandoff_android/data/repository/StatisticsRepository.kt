package com.onandoff.onandoff_android.data.repository

import com.onandoff.onandoff_android.data.model.StatisticsResponse

interface StatisticsRepository {
    suspend fun getMonthlyStatistics(profileId: Int): StatisticsResponse

    suspend fun getMonthlyLikesCount(profileId: Int): StatisticsResponse

    suspend fun getMonthlyMyFeedsCount(profileId: Int): StatisticsResponse

    suspend fun getMonthlyFollowersCount(profileId: Int): StatisticsResponse
}
