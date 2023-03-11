package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName

data class NotificationData (
    @SerializedName("statusCode") val statusCode: Int,
)

data class NotificationResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message") val message: String
)

data class TokenData (
    @SerializedName("alarmToken") val alarmToken: String
)