package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName

data class FeedResponseData(
    @SerializedName("feedId") val feedId: Int,
    @SerializedName("feedContent") val feedContent: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("feedImgList") val feedImgList: ArrayList<String>
)

