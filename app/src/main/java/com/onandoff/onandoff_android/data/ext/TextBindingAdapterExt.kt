package com.onandoff.onandoff_android.data.ext

import android.widget.TextView
import androidx.core.view.isGone
import androidx.databinding.BindingAdapter
import com.onandoff.onandoff_android.R
import java.text.SimpleDateFormat

@BindingAdapter("joinToTextList")
fun TextView.setJoinToTextList(textList: List<String>) {
    if (textList.isEmpty()) {
        this.text = ""
        this.isGone = true
    } else {
        this.text = textList.joinToString(" #", "#")
    }
}