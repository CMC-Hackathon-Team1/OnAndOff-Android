package com.onandoff.onandoff_android.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.data.model.MyProfile
import com.onandoff.onandoff_android.data.model.RelevantUser
import com.onandoff.onandoff_android.databinding.ItemRelevantUserBinding

class RelevantUserListAdapter(
    private val userProfileClick: (RelevantUser) -> Unit
): ListAdapter<RelevantUser, RelevantUserListAdapter.RelatedUserViewHolder>(RelatedUserDiffUtil) {

    class RelatedUserViewHolder(
        private val binding: ItemRelevantUserBinding,
        private val userProfileClick: (RelevantUser) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(relevantUser: RelevantUser, position: Int) {
            binding.tvRelatedUserProfile.text = relevantUser.name
            binding.viewLeft.isVisible = position == 0

            Glide.with(binding.root.context)
                .load(relevantUser.profileImageUrl)
                .into(binding.ivRelevantUserProfile)

            binding.root.setOnClickListener {
                userProfileClick(relevantUser)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedUserViewHolder {
        val binding = ItemRelevantUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RelatedUserViewHolder(binding, userProfileClick)
    }

    override fun onBindViewHolder(holder: RelatedUserViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    companion object RelatedUserDiffUtil : DiffUtil.ItemCallback<RelevantUser>() {
        override fun areItemsTheSame(oldItem: RelevantUser, newItem: RelevantUser): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: RelevantUser, newItem: RelevantUser): Boolean {
            return oldItem == newItem
        }
    }
}