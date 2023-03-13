package com.onandoff.onandoff_android.data.request

import com.google.gson.annotations.SerializedName

data class FollowRequest(
    @SerializedName("fromProfileId")
    val fromProfileId: Int,
    @SerializedName("toProfileId")
    val toProfileId: Int
)
