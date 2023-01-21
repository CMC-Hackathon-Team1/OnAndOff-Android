package com.onandoff.onandoff_android.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.MyProfileData
import com.onandoff.onandoff_android.data.model.LookAroundData
import com.onandoff.onandoff_android.databinding.ItemMyProfileBinding

class MyProfileListAdapter(
    private val onClick: (MyProfileData) -> Unit
): ListAdapter<MyProfileData, MyProfileListAdapter.MyProfileViewHolder>(MyProfileDiffUtil) {

    class MyProfileViewHolder(
        private val binding: ItemMyProfileBinding,
        private val onClick: (MyProfileData) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(myProfileData: MyProfileData, position: Int) {
            binding.tvMyProfile.text = myProfileData.name
            binding.viewLeft.isVisible = position == 0

            Glide.with(binding.root.context)
                .load(myProfileData.profileImageUrl)
                .into(binding.ivMyProfile)

            binding.root.setOnClickListener {
                onClick(myProfileData)
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

    companion object MyProfileDiffUtil : DiffUtil.ItemCallback<MyProfileData>() {
        override fun areItemsTheSame(oldItem: MyProfileData, newItem: MyProfileData): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: MyProfileData, newItem: MyProfileData): Boolean {
            return oldItem == newItem
        }
    }
}