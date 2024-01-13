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
    val profileImgUrl: String?
)

data class GetBlockedUserResponse(
    @SerializedName("profileId")
    val profileId: Int,
    @SerializedName("personaName")
    val personaName: String?,
    @SerializedName("profileName")
    val profileName: String?,
    @SerializedName("profileImgUrl")
    val profileImgUrl: String?
)

fun GetBlockedUserResponse.toEntity(): BlockedUser {
    return BlockedUser(
        profileId = profileId,
        personaName = personaName,
        profileName = profileName,
        profileImgUrl = profileImgUrl
    )
}

data class GetBlockedUserListResponse(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("message")
    val message: String?,
    @SerializedName("result")
    val result: List<GetBlockedUserResponse>
)
