package com.onandoff.onandoff_android.data.repository

import com.onandoff.onandoff_android.data.model.CalendarResponse
import com.onandoff.onandoff_android.data.remote.CalendarDataSource

class CalendarRepositoryImpl(
    private val calendarDataSource: CalendarDataSource
) : CalendarRepository {
    override suspend fun getCalendarFeedList(
        profileId: Int,
        year: Int,
        month: String
    ): CalendarResponse {
        return calendarDataSource.getCalendarFeedList(profileId, year, month)
    }
}
