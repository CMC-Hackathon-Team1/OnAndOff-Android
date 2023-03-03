package com.onandoff.onandoff_android.data.ext

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat

@BindingAdapter("timestamp")
fun TextView.setTimestamp(timestamp: String?) {
    timestamp ?: return

    val simpleDateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
//    val simpleDateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분")
    text = simpleDateFormat.format(timestamp.toString())
}



fun Long.convertDate(): String =
    SimpleDateFormat("yyyy년 MM월 dd일").toString()