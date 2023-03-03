package com.onandoff.onandoff_android.data.model

data class FeedData(
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
