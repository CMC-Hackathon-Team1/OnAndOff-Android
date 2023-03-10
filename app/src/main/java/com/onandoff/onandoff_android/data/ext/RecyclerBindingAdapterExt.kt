package com.onandoff.onandoff_android.data.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.onandoff.onandoff_android.data.model.LookAroundFeedData
import com.onandoff.onandoff_android.presentation.home.MyProfileListAdapter
import com.onandoff.onandoff_android.presentation.home.viewmodel.MyProfileItem
import com.onandoff.onandoff_android.presentation.look.FeedListAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@BindingAdapter("myPersonaItems")
fun RecyclerView.setMyPersonaItems(items: List<MyProfileItem>?) {
    items ?: return

    val adapter = this.adapter as? MyProfileListAdapter
    adapter?.submitList(items)
}

@BindingAdapter("lookAroundFeedItems")
fun RecyclerView.setLookAroundFeedItems(pagingDataLiveData: LiveData<Flow<PagingData<LookAroundFeedData>>>?) {
    pagingDataLiveData ?: return

    val activity = context as AppCompatActivity
    pagingDataLiveData.observe(activity) {
        activity.lifecycleScope.launch {
            it.collect {
                val adapter = this@setLookAroundFeedItems.adapter as? FeedListAdapter
                adapter?.submitData(it)
            }
        }
    }
}