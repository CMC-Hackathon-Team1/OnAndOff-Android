package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName

data class FeedData(
    val profileId: Int,
    val feedId: Int,
    val categoryId: Int,
    val hashTagList: List<String>,
    val content: String,
    val isSecret: String
)

data class FeedResponse(
    @SerializedName("statusCode")val statusCode:Int,
    @SerializedName("message")val message:String
)

data class FeedSimpleData(
    val profileId: Int,
    val feedId:Int
)

data class CategoryResponse(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String?,
    @SerializedName("result")
    val result: List<CategoryData>
)

data class CategoryData(
    val categoryId: Int,
    val categoryName:String
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
