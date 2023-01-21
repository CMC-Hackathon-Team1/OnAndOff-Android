package com.onandoff.onandoff_android.presentation.look

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.LookAroundData
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.ItemLookAroundBinding

class LookAroundListAdapter(
    private val onClick: (LookAroundData) -> Unit,
    private val onFollowClick: (LookAroundData) -> Unit,
    private val onLikeClick: (LookAroundData) -> Unit,
): ListAdapter<LookAroundData, LookAroundListAdapter.LookAroundViewHolder>(LookAroundDiffUtil) {

    private val lookAroundList = mutableListOf<LookAroundData>()

    class LookAroundViewHolder(
        private val binding: ItemLookAroundBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(lookAroundData: LookAroundData, position: Int) {
            binding.tvUserName.text = lookAroundData.name
            Glide.with(binding.root.context)
                .load(lookAroundData.profileImageUrl)
                .into(binding.ivUserProfile)

            binding.tvPostDate.text = lookAroundData.postDate.toString()

            if (lookAroundData.isFollowing) {
                Glide.with(binding.root.context)
                    .load(R.drawable.ic_is_following)
                    .into(binding.ivLike)
            } else {
                Glide.with(binding.root.context)
                    .load(R.drawable.ic_not_following)
                    .into(binding.ivLike)
            }

            if (lookAroundData.like) {
                Glide.with(binding.root.context)
                    .load(R.drawable.ic_heart_full)
                    .into(binding.ivLike)
            } else {
                Glide.with(binding.root.context)
                    .load(R.drawable.ic_heart_mono)
                    .into(binding.ivLike)
            }

            binding.tvDesc.text = lookAroundData.desc

            Glide.with(binding.root.context)
                .load(lookAroundData.imageList[0])
                .into(binding.ivThumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LookAroundViewHolder {
        val binding = ItemLookAroundBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return LookAroundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LookAroundViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    fun setLookAroundListOnTab(lookAroundDataList: List<LookAroundData>) {
        lookAroundList.clear()
        lookAroundList.addAll(lookAroundDataList)
        notifyDataSetChanged()
    }

    fun add(position: Int, lookAroundData: LookAroundData) {
        lookAroundList.add(position, lookAroundData)
        notifyItemInserted(position)
    }

    fun replaceItem(lookAroundData: LookAroundData) {
        val index = lookAroundList.indexOf(lookAroundData)
        lookAroundList[index] = lookAroundData
        notifyItemChanged(index)
    }

    fun delete(position: Int, lookAroundData: LookAroundData) {
        lookAroundList.remove(lookAroundData)
        notifyItemRemoved(position)
    }

    companion object LookAroundDiffUtil : DiffUtil.ItemCallback<LookAroundData>() {
        override fun areItemsTheSame(oldItem: LookAroundData, newItem: LookAroundData): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: LookAroundData, newItem: LookAroundData): Boolean {
            return oldItem == newItem
        }
    }
}