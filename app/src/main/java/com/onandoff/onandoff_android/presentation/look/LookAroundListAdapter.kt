package com.onandoff.onandoff_android.presentation.look

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.data.model.LookAround
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.ItemLookAroundBinding

class LookAroundListAdapter(
    private val onClick: (LookAround) -> Unit,
    private val onFollowClick: (LookAround) -> Unit,
    private val onLikeClick: (LookAround) -> Unit
): ListAdapter<LookAround, LookAroundListAdapter.LookAroundViewHolder>(LookAroundDiffUtil) {

    private val lookAroundList = mutableListOf<LookAround>()

    class LookAroundViewHolder(
        private val binding: ItemLookAroundBinding,
        private val onClick: (LookAround) -> Unit,
        private val onFollowClick: (LookAround) -> Unit,
        private val onLikeClick: (LookAround) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(lookAround: LookAround, position: Int) {
            binding.tvUserName.text = lookAround.name
            Glide.with(binding.root.context)
                .load(lookAround.profileImageUrl)
                .into(binding.ivUserProfile)

            binding.tvPostDate.text = lookAround.postDate.toString()

            if (lookAround.isFollowing) {
                Glide.with(binding.root.context)
                    .load(R.drawable.ic_is_following)
                    .into(binding.ivLike)
            } else {
                Glide.with(binding.root.context)
                    .load(R.drawable.ic_not_following)
                    .into(binding.ivLike)
            }

            if (lookAround.like) {
                Glide.with(binding.root.context)
                    .load(R.drawable.ic_heart_full)
                    .into(binding.ivLike)
            } else {
                Glide.with(binding.root.context)
                    .load(R.drawable.ic_heart_mono)
                    .into(binding.ivLike)
            }

            binding.tvDesc.text = lookAround.desc

            Glide.with(binding.root.context)
                .load(lookAround.imageList[0])
                .into(binding.ivThumbnail)


            binding.root.setOnClickListener {
                onClick(lookAround)
            }

            binding.ivAddToFollowingList.setOnClickListener {
                onFollowClick(lookAround)
                if (lookAround.isFollowing) {
                    binding.ivAddToFollowingList.setImageResource(R.drawable.ic_not_following)
                } else {
                    binding.ivAddToFollowingList.setImageResource(R.drawable.ic_is_following)
                }
            }

            binding.ivLike.setOnClickListener {
                onLikeClick(lookAround)
                if (lookAround.like) {
                    binding.ivLike.setImageResource(R.drawable.ic_heart_mono)
                } else {
                    binding.ivLike.setImageResource(R.drawable.ic_heart_full)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LookAroundViewHolder {
        val binding = ItemLookAroundBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return LookAroundViewHolder(binding, onClick, onFollowClick, onLikeClick)
    }

    override fun onBindViewHolder(holder: LookAroundViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    fun setLookAroundListOnTab(lookAroundList: List<LookAround>) {
        this.lookAroundList.clear()
        this.lookAroundList.addAll(lookAroundList)
        notifyDataSetChanged()
    }

    fun add(position: Int, lookAround: LookAround) {
        lookAroundList.add(position, lookAround)
        notifyItemInserted(position)
    }

    fun replaceItem(lookAround: LookAround) {
        val index = lookAroundList.indexOf(lookAround)
        lookAroundList[index] = lookAround
        notifyItemChanged(index)
    }

    fun delete(position: Int, lookAround: LookAround) {
        lookAroundList.remove(lookAround)
        notifyItemRemoved(position)
    }

    companion object LookAroundDiffUtil : DiffUtil.ItemCallback<LookAround>() {
        override fun areItemsTheSame(oldItem: LookAround, newItem: LookAround): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: LookAround, newItem: LookAround): Boolean {
            return oldItem == newItem
        }
    }
}