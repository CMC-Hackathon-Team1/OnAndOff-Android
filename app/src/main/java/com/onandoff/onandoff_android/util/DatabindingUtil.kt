package com.onandoff.onandoff_android.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapterHelper {

    @BindingAdapter("profileUrl")
    @JvmStatic
    fun setProfileImage(view: ImageView, url: String?) {
        url?.let {
            Glide.with(view.context).load(it).circleCrop().into(view)
        }
    }
}
