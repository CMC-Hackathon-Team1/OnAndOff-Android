package com.onandoff.onandoff_android.data.model

data class LookAroundData(
    val key: String = "",
    val profileImageUrl: String = "",
    val name: String = "",
    val postDate: List<String>,
    val isFollowing: Boolean = false,
    val like: Boolean = false,
    val likeCount: Int = 0,
    val desc: String = "",
    val imageList: List<String>
)
