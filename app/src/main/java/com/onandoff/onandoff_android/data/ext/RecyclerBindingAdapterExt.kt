package com.onandoff.onandoff_android.data.ext

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.model.BlockedUser
import com.onandoff.onandoff_android.data.model.LookAroundFeedData
import com.onandoff.onandoff_android.presentation.home.MyProfileListAdapter
import com.onandoff.onandoff_android.presentation.home.setting.BlockedUserListAdapter
import com.onandoff.onandoff_android.presentation.home.viewmodel.MyProfileItem
import com.onandoff.onandoff_android.presentation.look.LookAroundFeedListAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@BindingAdapter("myPersonaItems")
fun RecyclerView.setMyPersonaItems(items: List<MyProfileItem>?) {
    items ?: return

    val adapter = this.adapter as? MyProfileListAdapter
    adapter?.submitList(items)
}

@BindingAdapter("myPersonaItemStrokeColor")
fun MaterialCardView.setMyPersonaItemSelected(isSelected: Boolean) {
    val strokeColor = if (isSelected) {
        ContextCompat.getColor(context, R.color.color_main)
    } else {
        Color.TRANSPARENT
    }


    this.setCardBackgroundColor(ColorStateList.valueOf(strokeColor))
    this.setContentPadding(7, 7, 7, 7)

//    invalidate()
}

@BindingAdapter("lookAroundFeedItems")
fun RecyclerView.setLookAroundFeedItems(pagingDataLiveData: LiveData<Flow<PagingData<LookAroundFeedData>>>?) {
    pagingDataLiveData ?: return

    val activity = context as AppCompatActivity
    pagingDataLiveData.observe(activity) {
        activity.lifecycleScope.launch {
            it.collect {
                val adapter = this@setLookAroundFeedItems.adapter as? LookAroundFeedListAdapter
                adapter?.submitData(it)
            }
        }
    }
}

@BindingAdapter("blockedUsers")
fun RecyclerView.setBlockedUsers(items: List<BlockedUser>?) {
    items ?: return

    val adapter = this.adapter as? BlockedUserListAdapter
    adapter?.submitList(items)
}
