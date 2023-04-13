package com.onandoff.onandoff_android.data.request

import com.google.gson.annotations.SerializedName

data class BlockOrUnblockOtherUserRequest(
    @SerializedName("fromProfileId")
    val fromProfileId: Int,
    @SerializedName("toProfileId")
    val toProfileId: Int,
    @SerializedName("type")
    val type: String
)