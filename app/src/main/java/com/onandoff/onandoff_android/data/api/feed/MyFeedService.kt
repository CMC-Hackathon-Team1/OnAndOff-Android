
package com.onandoff.onandoff_android.data.api.feed

import com.google.gson.JsonElement
import com.onandoff.onandoff_android.data.model.getFeedResponeData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MyFeedService {
    @GET("/feeds/monthly")
    fun getMyFeed(
        @Query("baseProfileId") baseProfileId:Int,
        @Query("targetProfileId") targetProfileId:Int,
        @Query("year")year:Int,
        @Query("month")month:String,
        @Query("page")page:Int
    ): Call<getFeedResponeData>
}
