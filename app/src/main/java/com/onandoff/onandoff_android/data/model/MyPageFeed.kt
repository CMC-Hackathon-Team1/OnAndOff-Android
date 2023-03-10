package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName

data class getFeedResponeData(
    @SerializedName("statusCode")val statusCode :Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result: FeedListResponseData
)
data class getFeedResponse(
    @SerializedName("feedArray")val feedArray:ArrayList<FeedResponseData>
)
data class getFeedListRespone(
    @SerializedName("statusCode")val statusCode :Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result: FeedListResponseData
)

data class FeedResponseData(
    @SerializedName("feedId") val feedId: Int,
    @SerializedName("isLike") val isLike :Boolean,
    @SerializedName("likeNum")val likeNum:Int,
    @SerializedName("feedContent") val feedContent: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("feedImgList") val feedImgList: ArrayList<String>
)

data class FeedListResponseData(
    @SerializedName("feedArray")val feedArray: ArrayList<FeedResponseData>
)

