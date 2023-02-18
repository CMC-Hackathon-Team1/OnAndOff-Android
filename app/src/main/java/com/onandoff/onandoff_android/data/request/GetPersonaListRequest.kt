package com.onandoff.onandoff_android.data.request

import com.google.gson.annotations.SerializedName
import com.onandoff.onandoff_android.data.model.MyProfileResponse

data class GetPersonaListRequest(
    val personaList: List<MyProfileResponse>?
)
