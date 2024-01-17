package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName

data class LikeFollowResponse(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String?
)
