package com.onandoff.onandoff_android.data.request

import com.google.gson.annotations.SerializedName

data class ReportFeedRequest(
    @SerializedName("feedId")
    val feedId: Int,
    @SerializedName("reportedCategoryId")
    val reportedCategoryId: Int,
    @SerializedName("content")
    val content: String?
)