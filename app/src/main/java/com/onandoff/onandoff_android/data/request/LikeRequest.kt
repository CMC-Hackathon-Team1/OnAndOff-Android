package com.onandoff.onandoff_android.data.request

import com.google.gson.annotations.SerializedName

data class LikeRequest(
    @SerializedName("profileId")
    val profileId: Int,
    @SerializedName("feedId")
    val feedId: Int
)