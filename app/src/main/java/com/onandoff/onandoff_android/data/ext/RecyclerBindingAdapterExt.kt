package com.onandoff.onandoff_android.data.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onandoff.onandoff_android.data.model.MyProfileResponse
import com.onandoff.onandoff_android.presentation.home.MyProfileListAdapter

@BindingAdapter("myPersonaItems")
fun RecyclerView.setMyPersonaItems(items: List<MyProfileResponse>?) {
    items ?: return

    val adapter = this.adapter as? MyProfileListAdapter
    adapter?.submitList(items)
}