package com.onandoff.onandoff_android.data.request

import com.google.gson.annotations.SerializedName
import com.onandoff.onandoff_android.data.model.MyProfileResponse

data class GetPersonaRequest(
    val profileId: Int,
    val profileName: String,
    val personaName: String,
    val statusMessage: String,
    val image: String
)
