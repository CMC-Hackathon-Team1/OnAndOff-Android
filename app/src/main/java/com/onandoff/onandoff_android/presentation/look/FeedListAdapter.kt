package com.onandoff.onandoff_android.presentation.look

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.model.FeedData
import com.onandoff.onandoff_android.data.model.LookAroundFeedData
import com.onandoff.onandoff_android.databinding.ItemFeedListBinding

class FeedListAdapter(
    private val onProfileClick: (LookAroundFeedData) -> Unit,
    private val onFollowClick: (LookAroundFeedData) -> Unit,
    private val onLikeClick: (LookAroundFeedData) -> Unit,
    private val onOptionClick: (LookAroundFeedData) -> Unit
) : PagingDataAdapter<LookAroundFeedData, FeedListAdapter.FeedListViewHolder>(FeedListDiffUtil) {

    private val feedDataList = mutableListOf<LookAroundFeedData>()

    class FeedListViewHolder(
        private val binding: ItemFeedListBinding,
        private val onProfileClick: (LookAroundFeedData) -> Unit,
        private val onFollowClick: (LookAroundFeedData) -> Unit,
        private val onLikeClick: (LookAroundFeedData) -> Unit,
        private val onOptionClick: (LookAroundFeedData) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(feedData: LookAroundFeedData) {
            binding.feedItem = feedData
            binding.executePendingBindings()

            binding.layoutFeedProfile.setOnClickListener {
                onProfileClick(feedData)
            }

            binding.ivAddToFollowingList.setOnClickListener {
                onFollowClick(feedData)
            }

            binding.ivLike.setOnClickListener {
                onLikeClick(feedData)
            }

            binding.ivFeedOption.setOnClickListener {
                onOptionClick(feedData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedListViewHolder {
        val binding =
            ItemFeedListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FeedListViewHolder(
            binding,
            onProfileClick,
            onFollowClick,
            onLikeClick,
            onOptionClick
        )
    }

    override fun onBindViewHolder(holder: FeedListViewHolder, position: Int) {
        val feedData = getItem(position)
        if (feedData != null) {
            holder.bind(feedData)
        }
    }

    fun setLookAroundListOnTab(feedDataList: List<LookAroundFeedData>) {
        this.feedDataList.clear()
        this.feedDataList.addAll(feedDataList)
        notifyDataSetChanged()
    }

    fun add(position: Int, feedData: LookAroundFeedData) {
        feedDataList.add(position, feedData)
        notifyItemInserted(position)
    }

    fun replaceItem(feedData: LookAroundFeedData) {
        val index = feedDataList.indexOf(feedData)
        feedDataList[index] = feedData
        notifyItemChanged(index)
    }

    fun delete(position: Int, feedData: LookAroundFeedData) {
        feedDataList.remove(feedData)
        notifyItemRemoved(position)
    }

    companion object FeedListDiffUtil : DiffUtil.ItemCallback<LookAroundFeedData>() {
        override fun areItemsTheSame(
            oldItem: LookAroundFeedData,
            newItem: LookAroundFeedData
        ): Boolean {
            return false // oldItem.feedId == newItem.feedId
        }

        override fun areContentsTheSame(
            oldItem: LookAroundFeedData,
            newItem: LookAroundFeedData
        ): Boolean {
            return oldItem == newItem
        }
    }
}