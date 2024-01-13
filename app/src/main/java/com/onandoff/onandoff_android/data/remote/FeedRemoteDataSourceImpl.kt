package com.onandoff.onandoff_android.data.remote

import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.model.*
import com.onandoff.onandoff_android.data.request.FollowRequest
import com.onandoff.onandoff_android.data.request.LikeRequest
import com.onandoff.onandoff_android.data.request.ReportFeedRequest

class FeedRemoteDataSourceImpl(
    private val feedInterface: FeedInterface
) : FeedRemoteDataSource {
    override suspend fun getSearchFeedResult(
        profileId: Int,
        page: Int,
        categoryId: Int,
        fResult: Boolean,
        query: String
    ): GetFeedListResponse {
        if (query.isNotEmpty()) {
            return feedInterface.getSearchFeedResult(
                profileId,
                page,
                categoryId,
                fResult,
                query.ifEmpty { null }
            )
        } else {
            return feedInterface.getFeedListResult(
                profileId,
                page,
                categoryId,
                fResult
            )
        }
    }

    override suspend fun getFeedDetailResult(feedId: Int, profileId: Int): FeedDetailResponse {
        return feedInterface.getFeedDetailResult(feedId, profileId)
    }

    override suspend fun addOrUndoLike(likeRequest: LikeRequest): LikeFollowResponse {
        return feedInterface.like(likeRequest)
    }

    override suspend fun addOrUndoFollow(followRequest: FollowRequest): LikeFollowResponse {
        return feedInterface.follow(followRequest)
    }

    override suspend fun reportFeed(reportFeedRequest: ReportFeedRequest): ReportFeedResponse {
        return feedInterface.reportFeed(reportFeedRequest)
    }

    override suspend fun getCategories(): CategoryListResponse {
        return feedInterface.getCategories()
    }
}
