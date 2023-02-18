package com.onandoff.onandoff_android.data.remote

import com.onandoff.onandoff_android.data.api.user.StatisticsInterface
import com.onandoff.onandoff_android.data.model.StatisticsResponse

class StatisticsRemoteDataSourceImpl(
    private val statisticsInterface: StatisticsInterface
): StatisticsRemoteDataSource {

    override suspend fun getMonthlyStatistics(profileId: Int): StatisticsResponse {
        return statisticsInterface.getMonthlyStatistics(profileId)
    }

    override suspend fun getMonthlyLikesCount(profileId: Int): StatisticsResponse {
        return statisticsInterface.getMonthlyLikesCount(profileId)
    }

    override suspend fun getMonthlyMyFeedsCount(profileId: Int): StatisticsResponse {
        return statisticsInterface.getMonthlyMyFeedsCount(profileId)
    }

    override suspend fun getMonthlyFollowersCount(profileId: Int): StatisticsResponse {
        return statisticsInterface.getMonthlyFollowersCount(profileId)
    }
}