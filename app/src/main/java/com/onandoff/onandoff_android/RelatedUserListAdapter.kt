package com.onandoff.onandoff_android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onandoff.onandoff_android.databinding.ItemRelatedUserBinding

class RelatedUserListAdapter:
    ListAdapter<RelatedUserData, RelatedUserListAdapter.RelatedUserViewHolder>(RelatedUserDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedUserViewHolder {
        val binding = ItemRelatedUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RelatedUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RelatedUserViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }


    class RelatedUserViewHolder(
        private val binding: ItemRelatedUserBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(relatedUserData: RelatedUserData, position: Int) {

        }
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