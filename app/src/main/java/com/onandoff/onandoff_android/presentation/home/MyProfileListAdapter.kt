package com.onandoff.onandoff_android.presentation.home

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.model.MyProfileResponse
import com.onandoff.onandoff_android.databinding.ItemMyPersonaBinding

class MyProfileListAdapter(
    private val onClick: (MyProfileResponse) -> Unit
): ListAdapter<MyProfileResponse, MyProfileListAdapter.MyProfileViewHolder>(MyProfileDiffUtil) {

    class MyProfileViewHolder(
        private val binding: ItemMyPersonaBinding,
        private val onClick: (MyProfileResponse) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        private var isApplied = false

        fun bind(myProfileResponse: MyProfileResponse, position: Int) {
            binding.outerCvMyProfile.setContentPadding(7, 7, 7, 7)
            binding.root.setOnClickListener {
                if (isApplied) {
                    binding.outerCvMyProfile.setCardBackgroundColor(Color.TRANSPARENT)
                    isApplied = false
                } else {
                    binding.outerCvMyProfile.setCardBackgroundColor(Color.CYAN)
//                    binding.outerCvMyProfile.setCardBackgroundColor(ContextCompat.getColor(this, R.color.color_main))
                    isApplied = true
                }
                onClick(myProfileResponse)
            }

            binding.myPersona = myProfileResponse
            binding.executePendingBindings()
        }

        fun makeRoundedRectangleDrawable(
            radius: Float = 24f,
            strokeWidth: Int = 1,
            strokeColor: Int,
            backgroundColor: Int? = null
        ): Drawable = GradientDrawable().apply {
            shape = GradientDrawable.RING
            setStroke(strokeWidth, strokeColor)
            backgroundColor?.run { setColor(backgroundColor) }
            cornerRadii = floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProfileViewHolder {
        val binding = ItemMyPersonaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyProfileViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: MyProfileViewHolder, position: Int) {
        val myProfileResponse = getItem(position)
        holder.bind(myProfileResponse, position)
    }

    companion object MyProfileDiffUtil : DiffUtil.ItemCallback<MyProfileResponse>() {
        override fun areItemsTheSame(oldItem: MyProfileResponse, newItem: MyProfileResponse): Boolean {
            return oldItem.profileName == newItem.profileName
        }

        override fun areContentsTheSame(oldItem: MyProfileResponse, newItem: MyProfileResponse): Boolean {
            return oldItem == newItem
        }
    }
}