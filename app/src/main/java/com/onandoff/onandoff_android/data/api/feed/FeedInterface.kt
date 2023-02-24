package com.onandoff.onandoff_android.data.api.feed

import com.onandoff.onandoff_android.data.model.FeedData
import com.onandoff.onandoff_android.data.model.FeedDeleteData
import com.onandoff.onandoff_android.data.model.FeedResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface FeedInterface {
    @POST("/feeds")
    fun addFeedResponse(
        @Body body: FeedData
    ): Call<FeedResponse>

    @PATCH("/feeds")
    fun updateFeedResponse(
        @Body body: FeedData
    ): Call<FeedResponse>

    @PATCH("/feed/status")
    fun deleteFeedResponse(
        @Body body: FeedDeleteData
    ): Call<FeedResponse>
}