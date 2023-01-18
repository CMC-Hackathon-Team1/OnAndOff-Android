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
import com.onandoff.onandoff_android.databinding.ItemAddMyProfileBinding
import com.onandoff.onandoff_android.databinding.ItemMyProfileBinding

class MyProfileListAdapter(
    private val addProfileClick: () -> Unit
): ListAdapter<MyProfileData, RecyclerView.ViewHolder>(MyProfileDiffUtil) {

    class MyProfileViewHolder(
        private val binding: ItemMyProfileBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(myProfileData: MyProfileData, position: Int) {
            binding.tvMyProfile.text = myProfileData.name
            binding.viewLeft.isVisible = position == 0

            Glide.with(binding.root.context)
                .load(myProfileData.profileImageUrl)
                .into(binding.ivMyProfile)
        }
    }

    class AddMyProfileViewHolder(
        private val binding: ItemAddMyProfileBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(myProfileData: MyProfileData, addProfileClick: () -> Unit) {
            binding.viewRight.isVisible = true
            binding.root.setOnClickListener {
                addProfileClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val binding = ItemMyProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)

                MyProfileViewHolder(binding)
            }

            else -> {
                val binding = ItemAddMyProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)

                AddMyProfileViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyProfileViewHolder -> {
                holder.bind(getItem(position), position)
            }
            is AddMyProfileViewHolder -> {
                holder.bind(getItem(position), addProfileClick)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].isAlreadyAdded) {
            1
        } else {
            0
        }
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