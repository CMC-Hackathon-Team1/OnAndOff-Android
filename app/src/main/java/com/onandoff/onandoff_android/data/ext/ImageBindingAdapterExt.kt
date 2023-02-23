package com.onandoff.onandoff_android.data.ext

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.io.File

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?) {
    Glide.with(context)
        .load(url)
        .into(this)
}

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(file: File?) {
    Glide.with(context)
        .load(file)
        .into(this)
}

@BindingAdapter(value = ["imageUrl", "fallbackImage"])
fun ImageView.setImageUrl(url: String?, @DrawableRes fallbackImage: Int = -1) {
    when {
        !url.isNullOrEmpty() -> {
            Glide.with(context)
                .load(url)
                .into(this)
        }
        fallbackImage != -1 -> {
            Glide.with(context)
                .load(fallbackImage)
                .into(this)
        }
    }
}


@BindingAdapter(value = ["imageUrl", "fallbackImage"])
fun ImageView.setImageUrl(file: File?, @DrawableRes fallbackImage: Int = -1) {
    when {
        !file?.absolutePath.isNullOrEmpty() -> {
            Glide.with(context)
                .load(file)
                .into(this)
        }
        fallbackImage != -1 -> {
            Glide.with(context)
                .load(fallbackImage)
                .into(this)
        }
    }
}