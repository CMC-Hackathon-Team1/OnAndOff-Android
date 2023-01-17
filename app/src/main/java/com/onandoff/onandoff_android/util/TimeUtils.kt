package com.onandoff.onandoff_android.util

import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {
    // 시간 관련 함수들을 클래스로 모아둠

    companion object {
        private var instance: TimeUtils? = null

        fun getInstance(): TimeUtils {
            return instance ?: synchronized(this) {
                instance ?: TimeUtils().also {
                    instance = it
                }
            }
        }
    }

    fun convertTimeFormat(timestamp: Long, format: String?): String? {
        val dateFormat = SimpleDateFormat(format, Locale.KOREA)
        val date = Date()
        date.time = timestamp
        return dateFormat.format(date)
    }

    fun convertTimeFormat(date: Date?, format: String?): String? {
        date ?: return null
        val dateFormat = SimpleDateFormat(format, Locale.KOREA)
        return dateFormat.format(date)
    }
}