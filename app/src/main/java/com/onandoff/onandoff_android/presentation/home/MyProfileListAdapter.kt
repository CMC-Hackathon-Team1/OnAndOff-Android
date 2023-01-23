package com.onandoff.onandoff_android.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.data.model.MyPersona
import com.onandoff.onandoff_android.databinding.ItemMyProfileBinding

class MyProfileListAdapter(
    private val onClick: (MyPersona) -> Unit
): ListAdapter<MyPersona, MyProfileListAdapter.MyProfileViewHolder>(MyProfileDiffUtil) {

    class MyProfileViewHolder(
        private val binding: ItemMyProfileBinding,
        private val onClick: (MyPersona) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(myPersona: MyPersona, position: Int) {
            binding.tvMyProfile.text = myPersona.name
            binding.viewLeft.isVisible = position == 0

            Glide.with(binding.root.context)
                .load(myPersona.profileImageUrl)
                .into(binding.ivMyProfile)

            binding.root.setOnClickListener {
                onClick(myPersona)
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

    companion object MyProfileDiffUtil : DiffUtil.ItemCallback<MyPersona>() {
        override fun areItemsTheSame(oldItem: MyPersona, newItem: MyPersona): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: MyPersona, newItem: MyPersona): Boolean {
            return oldItem == newItem
        }
    }
}