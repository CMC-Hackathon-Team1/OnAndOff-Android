
package com.onandoff.onandoff_android.data.api.feed

import com.onandoff.onandoff_android.data.model.FeedResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MyFeedService {
    @GET("/feeds/my-feeds/by-month")
    fun getMyFeed(
        @Query("profileId") profileId:Int,
        @Query("year")year:Int,
        @Query("month")month:Int,
        @Query("page")page:Int
    ): Call<FeedResponseData>
}
