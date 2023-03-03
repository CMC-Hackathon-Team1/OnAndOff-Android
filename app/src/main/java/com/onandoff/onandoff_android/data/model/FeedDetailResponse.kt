package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName
import java.io.File

data class FeedDetailResponse(
    @SerializedName("feedImgList")
    val feedImgList: List<File>,
    @SerializedName("feedId")
    val feedId: Int,
    @SerializedName("personaName")
    val personaName: String,
    @SerializedName("profileName")
    val profileName: String,
    @SerializedName("feedContent")
    val feedContent: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("isLike")
    val isLike: Boolean
)