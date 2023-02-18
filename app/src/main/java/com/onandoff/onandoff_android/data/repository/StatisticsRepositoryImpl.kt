package com.onandoff.onandoff_android.data.repository

import com.onandoff.onandoff_android.data.model.StatisticsResponse
import com.onandoff.onandoff_android.data.remote.StatisticsRemoteDataSource

class StatisticsRepositoryImpl(
    private val statisticsRemoteDataSource: StatisticsRemoteDataSource
) : StatisticsRepository {

    override suspend fun getMonthlyStatistics(profileId: Int): StatisticsResponse {
        return statisticsRemoteDataSource.getMonthlyStatistics(profileId)
    }

    override suspend fun getMonthlyLikesCount(profileId: Int): StatisticsResponse {
        return statisticsRemoteDataSource.getMonthlyLikesCount(profileId)
    }

    override suspend fun getMonthlyMyFeedsCount(profileId: Int): StatisticsResponse {
        return statisticsRemoteDataSource.getMonthlyMyFeedsCount(profileId)
    }

    override suspend fun getMonthlyFollowersCount(profileId: Int): StatisticsResponse {
        return statisticsRemoteDataSource.getMonthlyFollowersCount(profileId)
    }
}