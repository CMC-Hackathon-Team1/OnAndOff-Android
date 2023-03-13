package com.onandoff.onandoff_android.presentation.home

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
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

        fun bind(myProfileResponse: MyProfileItem) {
            binding.outerCvMyProfile.setContentPadding(7, 7, 7, 7)
            binding.root.setOnClickListener {
                onClick(myProfileResponse)
            }

            val strokeColor =
                if (myProfileResponse.isSelected) {
                    ContextCompat.getColor(itemView.context, R.color.color_main)
                } else {
                    Color.TRANSPARENT
                }

            binding.outerCvMyProfile.setCardBackgroundColor(strokeColor)

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
            cornerRadii =
                floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProfileViewHolder {
        val binding =
            ItemMyPersonaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyProfileViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: MyProfileViewHolder, position: Int) {
        val myProfileResponse = getItem(position)
        holder.bind(myProfileResponse)
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