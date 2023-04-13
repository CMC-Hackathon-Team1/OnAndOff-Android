package com.onandoff.onandoff_android.data.model

import com.google.gson.annotations.SerializedName

data class BlockOtherUserResponse(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String?
)

data class BlockedUser(
    val profileId: Int,
    val personaName: String?,
    val profileName: String?,
    val statusMessage: String?,
    val profileImgUrl: String?,
    val createdAt: String?
)

data class GetBlockedUserResponse(
    @SerializedName("profileId")
    val profileId: Int,
    @SerializedName("personaName")
    val personaName: String?,
    @SerializedName("profileName")
    val profileName: String?,
    @SerializedName("statusMessage")
    val statusMessage: String?,
    @SerializedName("profileImgUrl")
    val profileImgUrl: String?,
    @SerializedName("createdAt")
    val createdAt: String?
)

data class GetBlockedUserListResponse(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String?,
    @SerializedName("result")
    val result: List<GetBlockedUserResponse>
)