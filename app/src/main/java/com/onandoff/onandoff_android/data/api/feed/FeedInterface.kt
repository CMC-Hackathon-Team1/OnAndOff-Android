package com.onandoff.onandoff_android.data.api.feed

import com.onandoff.onandoff_android.data.model.FeedData
import com.onandoff.onandoff_android.data.model.FeedDeleteData
import com.onandoff.onandoff_android.data.model.FeedReadData
import com.onandoff.onandoff_android.data.model.FeedResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface FeedInterface {
    @POST("/feeds")
    fun addFeedResponse(
        @Body body: FeedData
    ): Call<FeedResponse>

    @GET("/feeds/{feedId}/profiles/{profileId}")
    fun readFeedResponse(
        @Path("feedId") feedId: Int, @Path("profileId") profileId: Int
    ): Call<FeedReadData>

    @PATCH("/feeds")
    fun updateFeedResponse(
        @Body body: FeedData
    ): Call<FeedResponse>

    @PATCH("/feeds/status")
    fun deleteFeedResponse(
        @Body body:FeedDeleteData
    ): Call<FeedResponse>
}