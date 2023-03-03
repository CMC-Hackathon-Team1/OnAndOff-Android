package com.onandoff.onandoff_android.presentation.look

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
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
            binding.tvPersonaName.text = feedData.personaName
            Glide.with(binding.root.context)
                .load(feedData.profileImg)
                .into(binding.ivUserPersona)

            binding.tvPostDate.text = feedData.createdAt

            Log.d(
                "feedData.personaName, like, follow",
                "personaName : ${feedData.personaName}, isLike: ${feedData.isLike}, isFollowing: ${feedData.isFollowing}"
            )
            if (feedData.isLike) {
                Glide.with(binding.root.context)
                    .load(R.drawable.ic_heart_full)
                    .into(binding.ivLike)
            } else {
                Glide.with(binding.root.context)
                    .load(R.drawable.ic_heart_mono)
                    .into(binding.ivLike)
            }

            // 팔로우 여부에 따라 팔로잉 아이콘 변경하기
            if (feedData.isFollowing) {
                binding.ivAddToFollowingList.setImageResource(R.drawable.ic_is_following)
            } else {
                binding.ivAddToFollowingList.setImageResource(R.drawable.ic_not_following)
            }

            binding.tvDesc.text = feedData.feedContent

            val firstImage = feedData.feedImgList.firstOrNull()
            Log.d("firstImage", "$firstImage")
            Glide.with(binding.root.context)
                .load(firstImage)
                .into(binding.ivThumbnail)

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

        return FeedListViewHolder(binding, onProfileClick, onFollowClick, onLikeClick, onOptionClick)
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
        override fun areItemsTheSame(oldItem: LookAroundFeedData, newItem: LookAroundFeedData): Boolean {
            return oldItem.profileId == newItem.profileId
        }

        override fun areContentsTheSame(oldItem: LookAroundFeedData, newItem: LookAroundFeedData): Boolean {
            return oldItem == newItem
        }
    }
}