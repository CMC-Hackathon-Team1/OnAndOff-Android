package com.onandoff.onandoff_android.data.remote

import com.onandoff.onandoff_android.data.model.*
import com.onandoff.onandoff_android.data.request.FollowRequest
import com.onandoff.onandoff_android.data.request.LikeRequest
import com.onandoff.onandoff_android.data.request.ReportFeedRequest

interface FeedRemoteDataSource {
    suspend fun getSearchFeedResult(
        profileId: Int,
        page: Int,
        categoryId: Int,
        fResult: Boolean,
        query: String
    ): GetFeedListResponse

    suspend fun getFeedDetailResult(feedId: Int, profileId: Int): FeedDetailResponse

    suspend fun addOrUndoLike(likeRequest: LikeRequest): LikeFollowResponse

    suspend fun addOrUndoFollow(followRequest: FollowRequest): LikeFollowResponse

    suspend fun reportFeed(reportFeedRequest: ReportFeedRequest): ReportFeedResponse

    suspend fun getCategories(): CategoryListResponse
}
