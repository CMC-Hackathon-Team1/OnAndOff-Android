package com.onandoff.onandoff_android.data.remote

import com.onandoff.onandoff_android.data.model.CalendarResponse

interface CalendarDataSource {
    suspend fun getCalendarFeedList(profileId: Int, year: Int, month: String) : CalendarResponse
}
