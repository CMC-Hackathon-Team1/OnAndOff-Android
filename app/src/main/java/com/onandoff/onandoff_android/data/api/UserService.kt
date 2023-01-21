package com.onandoff.onandoff_android.data.api


interface UserService {

}
//    @GET("users/{profileId}/profile/statistics")
//    suspend fun getStatistics(
//        @Path("profileId") profileId: Int
//    ): Response<StatisticsResponse>
//
//    @GET("users/{profileId}/profile")
//    suspend fun getAllProfile(
//        @Path("profileId") profileId: Int
//    ): Response<AllProfileResponse>
//
//    @GET("calendars")
//    suspend fun getCalendarData(
//        @Query("year") year: String,
//        @Query("month") month: String
//    ): Response<CalendarDataResponse>
//
//    @GET("feeds/dates/profiles/{profileId}")
//    suspend fun getDayDetails(
//        @Path("profileId") profileId: Int,
//        @Query("year") year: String,
//        @Query("month") month: String,
//        @Query("day") day: String
//    ): Response<DayDetailResponse>