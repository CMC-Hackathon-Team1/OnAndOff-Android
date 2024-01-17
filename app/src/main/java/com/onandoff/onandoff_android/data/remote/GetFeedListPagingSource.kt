package com.onandoff.onandoff_android.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onandoff.onandoff_android.data.model.FeedResponse

class GetFeedListPagingSource(
    private val feedRemoteDataSource: FeedRemoteDataSource
) : PagingSource<Int, FeedResponse>() {
    override fun getRefreshKey(state: PagingState<Int, FeedResponse>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FeedResponse> {
        val page = params.key ?: 1

        val startPosition = params.loadSize * (page - 1) + 1

        error("")
//        val response = feedRemoteDataSource.getFeedListResult()
//
//        return feedRemoteDataSource.getFeedListResult(
//            profileId = profileId,
//            page = page,
//            categoryId = categoryId,
//            fResult = fResult
//        ).
    }
}
