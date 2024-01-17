package com.onandoff.onandoff_android.data.repository

import com.onandoff.onandoff_android.data.model.CalendarResponse

interface CalendarRepository {
    suspend fun getCalendarFeedList(profileId: Int, year: Int, month: String) : CalendarResponse
}
