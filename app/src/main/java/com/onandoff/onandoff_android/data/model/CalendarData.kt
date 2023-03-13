package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName

data class CalendarData(
    val day: String,
    val feedId: String,
    val feedImgUrl: String
)

data class CalendarResponse(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String?,
    @SerializedName("result")
    val result: List<CalendarData>
)