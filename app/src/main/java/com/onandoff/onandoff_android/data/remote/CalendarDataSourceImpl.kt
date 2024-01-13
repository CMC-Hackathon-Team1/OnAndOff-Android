package com.onandoff.onandoff_android.data.remote

import com.onandoff.onandoff_android.data.api.feed.CalendarInterface
import com.onandoff.onandoff_android.data.model.CalendarResponse
import com.onandoff.onandoff_android.data.repository.CalendarRepository

class CalendarDataSourceImpl(
    private val calendarInterface: CalendarInterface
) : CalendarDataSource {
    override suspend fun getCalendarFeedList(
        profileId: Int,
        year: Int,
        month: String
    ): CalendarResponse {
        return calendarInterface.getCalendarListResponse(profileId, year, month)
    }

}
