package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName


data class LookAroundFeedData(
    val feedId: Int,
    val profileId: Int,
    val profileImg: String = "",
    val personaName: String = "",
    val profileName: String = "",
    val feedContent: String = "",
    val createdAt: String,
    var isLike: Boolean = false,
    var isFollowing: Boolean = false,
    val feedImgList: List<String>,
    val hashTagList: List<String>
)

data class LookAroundFeedResponse(
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

data class FeedData(
    val profileId: Int,
    val categoryId: Int,
    val hashTagList: List<String>,
    val content: String,
    val isSecret: String
)

data class FeedResponse(
    @SerializedName("statusCode")val statusCode:Int,
    @SerializedName("message")val message:String
)

data class FeedDeleteData(
    val profileId: Int,
    val feedId:Int
)

data class FeedReadData(
    val feedId: Int,
    val categoryId: Int,
    val feedImgList: List<String>,
    val hashTagList: List<String>,
    val personaName: String,
    val profileName: String,
    val feedContent: String,
    val profileImg: String,
    val createdAt: String,
    val isLike: Boolean
)