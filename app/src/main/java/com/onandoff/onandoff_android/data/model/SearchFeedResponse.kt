package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName
import java.io.File

data class SearchFeedResponse(
    @SerializedName("feedImgList")
    val feedImgList: List<String>,
    @SerializedName("isLike")
    val isLike: Boolean,
    @SerializedName("isFollowing")
    val isFollowing: Boolean,
    @SerializedName("hashTagList")
    val hashTagList: List<String>,
    @SerializedName("feedId")
    val feedId: Int,
    @SerializedName("profileId")
    val profileId: Int,     // 본인 게시글 제외 기능 + 좋아요, 팔로우 여부 확인 기능을 위한 것
    @SerializedName("profileName")
    val profileName: String,
    @SerializedName("profileImg")
    val profileImg: String,
    @SerializedName("personaName")
    val personaName: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("feedContent")
    val feedContent: String
)
