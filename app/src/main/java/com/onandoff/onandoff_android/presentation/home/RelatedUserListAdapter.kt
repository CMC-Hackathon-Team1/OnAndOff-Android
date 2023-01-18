package com.onandoff.onandoff_android.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.RelatedUserData
import com.onandoff.onandoff_android.databinding.ItemRelatedUserBinding

class RelatedUserListAdapter:
    ListAdapter<RelatedUserData, RelatedUserListAdapter.RelatedUserViewHolder>(RelatedUserDiffUtil) {

    class RelatedUserViewHolder(
        private val binding: ItemRelatedUserBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(relatedUserData: RelatedUserData, position: Int) {
            binding.tvRelatedUserProfile.text = relatedUserData.name
            binding.viewLeft.isVisible = position == 0

            Glide.with(binding.root.context)
                .load(relatedUserData.profileImageUrl)
                .into(binding.ivRelatedUserProfile)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedUserViewHolder {
        val binding = ItemRelatedUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RelatedUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RelatedUserViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    companion object RelatedUserDiffUtil : DiffUtil.ItemCallback<RelatedUserData>() {
        override fun areItemsTheSame(oldItem: RelatedUserData, newItem: RelatedUserData): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: RelatedUserData, newItem: RelatedUserData): Boolean {
            return oldItem == newItem
        }
    }
}