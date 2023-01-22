package com.onandoff.onandoff_android.data.model

data class Posting(
    val postingId: Int,
    val hashtag: String,
    val category: Int,
    val content: String,
    val isPrivate: Boolean,
    val createdAt: String,
    val imageList: List<String>
)
