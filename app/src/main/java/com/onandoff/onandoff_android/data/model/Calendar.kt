package com.onandoff.onandoff_android.data.model

data class Calendar(
    val day: String,
    val isExist: Boolean = false,
    val isCurrentMonth: Boolean = false,
    val imageUrl: String = ""
)