package com.onandoff.onandoff_android.presentation.home.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onandoff.onandoff_android.data.model.BlockedUser
import com.onandoff.onandoff_android.databinding.ItemBlockedUserBinding

class BlockedUserListAdapter(
    private val onButtonClick: (BlockedUser) -> Unit
) : ListAdapter<BlockedUser, BlockedUserListAdapter.BlockedUserViewHolder>(BlockedUserDiffUtil) {

    class BlockedUserViewHolder(
         private val binding: ItemBlockedUserBinding,
         private val onClick: (BlockedUser) -> Unit
     ) : RecyclerView.ViewHolder(binding.root) {

         fun bind(blockedUserItem: BlockedUser) {
             binding.blockedUserItem = blockedUserItem

             binding.btnUnblock.setOnClickListener {
                 onClick(blockedUserItem)
             }

             binding.executePendingBindings()
         }
     }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockedUserViewHolder {
         val binding =
             ItemBlockedUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
         return BlockedUserViewHolder(binding, onButtonClick)
     }

     override fun onBindViewHolder(holder: BlockedUserViewHolder, position: Int) {
         val blockedUserItem = getItem(position)
         holder.bind(blockedUserItem)
     }

     companion object BlockedUserDiffUtil : DiffUtil.ItemCallback<BlockedUser>() {
         override fun areItemsTheSame(oldItem: BlockedUser, newItem: BlockedUser): Boolean {
             return oldItem.profileId == newItem.profileId
         }

         override fun areContentsTheSame(oldItem: BlockedUser, newItem: BlockedUser): Boolean {
             return oldItem == newItem
         }
     }
}