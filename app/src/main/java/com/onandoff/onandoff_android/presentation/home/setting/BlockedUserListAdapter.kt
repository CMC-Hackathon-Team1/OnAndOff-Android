package com.onandoff.onandoff_android.presentation.home.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onandoff.onandoff_android.data.model.BlockedUser
import com.onandoff.onandoff_android.data.model.GetBlockedUserResponse
import com.onandoff.onandoff_android.databinding.ItemBlockedUserBinding

class BlockedUserListAdapter(
    private val onButtonClick: (GetBlockedUserResponse) -> Unit
) : ListAdapter<GetBlockedUserResponse, BlockedUserListAdapter.BlockedUserViewHolder>(BlockedUserDiffUtil) {

    private val blockedUserList = mutableListOf<GetBlockedUserResponse>()

    inner class BlockedUserViewHolder(
         private val binding: ItemBlockedUserBinding,
         private val onClick: (GetBlockedUserResponse) -> Unit
     ) : RecyclerView.ViewHolder(binding.root) {

         fun bind(blockedUserItem: GetBlockedUserResponse) {
             binding.blockedUserItem = blockedUserItem

             binding.btnUnblock.setOnClickListener {
                 onClick(blockedUserItem)
                 blockedUserList.add(blockedUserItem)
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

    fun getItem(): List<GetBlockedUserResponse> {
        return blockedUserList
    }

     companion object BlockedUserDiffUtil : DiffUtil.ItemCallback<GetBlockedUserResponse>() {
         override fun areItemsTheSame(oldItem: GetBlockedUserResponse, newItem: GetBlockedUserResponse): Boolean {
             return oldItem.profileId == newItem.profileId
         }

         override fun areContentsTheSame(oldItem: GetBlockedUserResponse, newItem: GetBlockedUserResponse): Boolean {
             return oldItem == newItem
         }
     }
}