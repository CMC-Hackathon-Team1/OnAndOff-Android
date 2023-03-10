package com.onandoff.onandoff_android.data.repository

import androidx.paging.*
import com.onandoff.onandoff_android.data.model.*
import com.onandoff.onandoff_android.data.remote.FeedRemoteDataSource
import com.onandoff.onandoff_android.data.remote.SearchFeedListPagingSource
import com.onandoff.onandoff_android.data.request.FollowRequest
import com.onandoff.onandoff_android.data.request.LikeRequest
import com.onandoff.onandoff_android.data.request.ReportFeedRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class FeedRequest(
    val profileId: Int,
    val categoryId: Int,
    val fResult: Boolean,
    val query: String = ""
)

class FeedRepositoryImpl(
    private val feedRemoteDataSource: FeedRemoteDataSource
) : FeedRepository {
    override suspend fun getFeedListSource(request: FeedRequest): Flow<PagingData<LookAroundFeedData>> {

        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10
            ),
            initialKey = SearchFeedListPagingSource.INITIAL_KEY,
            pagingSourceFactory = {
                SearchFeedListPagingSource(feedRemoteDataSource, request)
            }
        ) // 하단에 mapping data class 만듦
            .flow
            .map {
                it.map {
                    it.toEntity()
                }
            }
    }

    override suspend fun getFeedDetailResult(feedId: Int, profileId: Int): FeedDetailResponse {
        return feedRemoteDataSource.getFeedDetailResult(feedId, profileId)
    }

    override suspend fun addOrUndoLike(likeRequest: LikeRequest): LikeFollowResponse {
        return feedRemoteDataSource.addOrUndoLike(likeRequest)
    }

    override suspend fun addOrUndoFollow(followRequest: FollowRequest): LikeFollowResponse {
        return feedRemoteDataSource.addOrUndoFollow(followRequest)
    }

    override suspend fun reportFeed(reportFeedRequest: ReportFeedRequest): ReportFeedResponse {
        return feedRemoteDataSource.reportFeed(reportFeedRequest)
    }

    override suspend fun getCategories(): CategoryListResponse {
        return feedRemoteDataSource.getCategories()
    }
}

fun LookAroundFeedResponse.toEntity(): LookAroundFeedData {
    return LookAroundFeedData(
        feedId = feedId,
        profileId = profileId,
        profileImg = profileImg,
        personaName = personaName,
        profileName = profileName,
        feedContent = feedContent,
        createdAt = createdAt,
        isLike = isLike,
        isFollowing = isFollowing,
        feedImgList = feedImgList,
        hashTagList = hashTagList
    )
}