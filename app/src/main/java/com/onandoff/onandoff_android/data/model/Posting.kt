package com.onandoff.onandoff_android.data.model

data class Posting(
    var feedId: Int,
    var hashtag: String,
    var category: String,
    var content: String,
    var status: Boolean,
    var imageUrl: String
)
