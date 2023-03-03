package com.onandoff.onandoff_android.data.api.feed

import com.onandoff.onandoff_android.data.model.CalendarResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarInterface {
    @GET("/feeds/my-feeds/in-calendar")
    fun getCalendarList(
        @Query("profileId") profileId: Int,
        @Query("year") year: Int,
        @Query("month") month: String
    ): Call<CalendarResponse>
}