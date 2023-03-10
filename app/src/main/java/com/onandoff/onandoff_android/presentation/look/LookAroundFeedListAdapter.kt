package com.onandoff.onandoff_android.presentation.look

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.onandoff.onandoff_android.data.model.LookAroundFeedData
import com.onandoff.onandoff_android.databinding.ItemFeedListBinding

class LookAroundFeedListAdapter(
    private val onProfileClick: (LookAroundFeedData) -> Unit,
    private val onFollowClick: (LookAroundFeedData) -> Unit,
    private val onLikeClick: (LookAroundFeedData) -> Unit,
    private val onOptionClick: (LookAroundFeedData) -> Unit
) : PagingDataAdapter<LookAroundFeedData, LookAroundFeedListAdapter.FeedListViewHolder>(FeedListDiffUtil) {

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