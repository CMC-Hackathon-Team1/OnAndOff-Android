package com.onandoff.onandoff_android.data.request

import java.io.File

data class CreateProfileRequest(
    val profileName: String,
    val personaName: String,
    val statusMessage: String,
    val imagePath: File
)