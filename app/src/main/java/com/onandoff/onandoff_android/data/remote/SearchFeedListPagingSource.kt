package com.onandoff.onandoff_android.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onandoff.onandoff_android.data.model.FeedResponse
import com.onandoff.onandoff_android.data.model.LookAroundFeedResponse
import com.onandoff.onandoff_android.data.repository.FeedRequest

class SearchFeedListPagingSource(
    private val feedRemoteDataSource: FeedRemoteDataSource,
    private val request: FeedRequest
) : PagingSource<Int, LookAroundFeedResponse>() {
    init {
        Log.d("feedRequest", "${hashCode()}")
    }
    override fun getRefreshKey(state: PagingState<Int, LookAroundFeedResponse>): Int {
        return INITIAL_KEY
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LookAroundFeedResponse> {
        val page = params.key ?: 1
        Log.d("feedRequest page", "$page")

        val feedResponse =
            kotlin.runCatching {
                feedRemoteDataSource.getSearchFeedResult(
                    profileId = request.profileId,
                    page = page,
                    categoryId = request.categoryId,
                    fResult = request.fResult,
                    query = request.query
                )
            }
                .onFailure {
                    return LoadResult.Error(it)
                }

        val list = feedResponse.getOrNull()?.result.orEmpty()

        return LoadResult.Page(
            data = list,
            prevKey = null,
            nextKey = if (list.isNotEmpty()) page + 1 else null
        )

        // 리스트가 비어 있는가?
        // 1페이지 2개. 2페이지 요청 그냥 해봄. 2페이지 0개. 3페이지 요청 안함.

        // 받은 리스트가 10개 미만인가?
        // 1페이지 2개. 2페이지 요청 안함.
    }

    companion object {
        const val INITIAL_KEY = 1
    }
}
