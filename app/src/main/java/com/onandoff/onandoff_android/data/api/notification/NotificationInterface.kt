package com.onandoff.onandoff_android.data.api.notification

import com.onandoff.onandoff_android.data.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface NotificationInterface {
    @PATCH("/alarms/notice")
    fun updateNoticeAlarmResponse(
        @Body body: NotificationData
    ): Call<NotificationResponse>

    @PATCH("/alarms/following")
    fun updateFollowAlarmResponse(
        @Body body: NotificationData
    ): Call<NotificationResponse>

    @PATCH("/alarms/like")
    fun updateLikeAlarmResponse(
        @Body body: NotificationData
    ): Call<NotificationResponse>

    @POST("/alarms/token")
    fun setAlarmTokenResponse(
        @Body body: TokenData
    ): Call<NotificationResponse>
}