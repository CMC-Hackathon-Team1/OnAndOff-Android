package com.onandoff.onandoff_android.presentation.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.ItemMyPersonaBinding
import com.onandoff.onandoff_android.presentation.home.viewmodel.MyProfileItem

class MyProfileListAdapter(
    private val onClick: (MyProfileItem) -> Unit
) : ListAdapter<MyProfileItem, MyProfileListAdapter.MyProfileViewHolder>(MyProfileDiffUtil) {

    class MyProfileViewHolder(
        private val binding: ItemMyPersonaBinding,
        private val onClick: (MyProfileItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(myProfileItem: MyProfileItem) {
            binding.root.setOnClickListener {
                onClick(myProfileItem)
            }

            val strokeColor =
                if (myProfileItem.isSelected) {
                    ContextCompat.getColor(itemView.context, R.color.color_main)
                } else {
                    Color.TRANSPARENT
                }

            binding.outerCvMyProfile.setContentPadding(7, 7, 7, 7)
            binding.outerCvMyProfile.setCardBackgroundColor(strokeColor)

            binding.myPersona = myProfileItem
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProfileViewHolder {
        val binding =
            ItemMyPersonaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyProfileViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: MyProfileViewHolder, position: Int) {
        val myProfile = getItem(position)
        holder.bind(myProfile)
    }

    companion object MyProfileDiffUtil : DiffUtil.ItemCallback<MyProfileItem>() {
        override fun areItemsTheSame(oldItem: MyProfileItem, newItem: MyProfileItem): Boolean {
            return oldItem.myProfile.profileId == newItem.myProfile.profileId
        }

        override fun areContentsTheSame(oldItem: MyProfileItem, newItem: MyProfileItem): Boolean {
            return oldItem == newItem
        }
    }
}