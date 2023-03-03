package com.onandoff.onandoff_android.data.repository

import androidx.paging.PagingData
import com.onandoff.onandoff_android.data.model.*
import com.onandoff.onandoff_android.data.request.FollowRequest
import com.onandoff.onandoff_android.data.request.LikeRequest
import com.onandoff.onandoff_android.data.request.ReportFeedRequest
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    suspend fun getFeedListSource(request: FeedRequest): Flow<PagingData<LookAroundFeedData>>

    suspend fun getFeedDetailResult(feedId: Int, profileId: Int): FeedDetailResponse

    suspend fun addOrUndoLike(likeRequest: LikeRequest): LikeFollowResponse

    suspend fun addOrUndoFollow(followRequest: FollowRequest): LikeFollowResponse

    suspend fun reportFeed(reportFeedRequest: ReportFeedRequest): ReportFeedResponse

    suspend fun getCategories(): CategoryListResponse
}

