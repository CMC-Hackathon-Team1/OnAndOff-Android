package com.onandoff.onandoff_android.data.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onandoff.onandoff_android.presentation.home.MyProfileListAdapter
import com.onandoff.onandoff_android.presentation.home.viewmodel.MyProfileItem

@BindingAdapter("myPersonaItems")
fun RecyclerView.setMyPersonaItems(items: List<MyProfileItem>?) {
    items ?: return

    val adapter = this.adapter as? MyProfileListAdapter
    adapter?.submitList(items)
}