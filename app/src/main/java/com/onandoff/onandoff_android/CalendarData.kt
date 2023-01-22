package com.onandoff.onandoff_android

data class CalendarData(
    val day: String,
    val isExist: Boolean = false,
    val isCurrentMonth: Boolean = false,
    val imageUrl: String = ""
)