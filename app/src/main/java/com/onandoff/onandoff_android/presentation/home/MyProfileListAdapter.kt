package com.onandoff.onandoff_android.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.data.model.MyPersonaData
import com.onandoff.onandoff_android.databinding.ItemMyProfileBinding

class MyProfileListAdapter(
    private val onClick: (MyPersonaData) -> Unit
): ListAdapter<MyPersonaData, MyProfileListAdapter.MyProfileViewHolder>(MyProfileDiffUtil) {

    class MyProfileViewHolder(
        private val binding: ItemMyProfileBinding,
        private val onClick: (MyPersonaData) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(myPersonaData: MyPersonaData, position: Int) {
            binding.tvMyProfile.text = myPersonaData.name
            binding.viewLeft.isVisible = position == 0

            Glide.with(binding.root.context)
                .load(myPersonaData.profileImageUrl)
                .into(binding.ivMyProfile)

            binding.root.setOnClickListener {
                onClick(myPersonaData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProfileViewHolder {
        val binding = ItemMyProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyProfileViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: MyProfileViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    companion object MyProfileDiffUtil : DiffUtil.ItemCallback<MyPersonaData>() {
        override fun areItemsTheSame(oldItem: MyPersonaData, newItem: MyPersonaData): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: MyPersonaData, newItem: MyPersonaData): Boolean {
            return oldItem == newItem
        }
    }
}