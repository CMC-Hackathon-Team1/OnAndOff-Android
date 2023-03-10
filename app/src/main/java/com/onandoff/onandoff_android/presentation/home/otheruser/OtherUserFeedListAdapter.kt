package com.onandoff.onandoff_android.presentation.home.otheruser

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.onandoff.onandoff_android.data.model.FeedResponseData
import com.onandoff.onandoff_android.databinding.ItemMypageUserfeedBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("NotifyDataSetChanged")
class OtherUserFeedListAdapter(private var feedList : List<FeedResponseData>) : RecyclerView.Adapter<OtherUserFeedListAdapter.OtherUserFeedListViewHolder>() {

    inner class OtherUserFeedListViewHolder(val binding: ItemMypageUserfeedBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(write: FeedResponseData){
            binding.tvMypageRvItemPostText.text = write.feedContent
            binding.tvMypageRvItemDate.text = write.createdAt
            binding.tvMypageRvItemLike.text = write.likeNum.toString()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherUserFeedListViewHolder {
        val viewBinding = ItemMypageUserfeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OtherUserFeedListViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: OtherUserFeedListViewHolder, position: Int) {
        holder.bind(feedList[position])
    }

    override fun getItemCount(): Int = feedList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setItems(item: List<FeedResponseData>) {
        feedList = item
        notifyDataSetChanged()
    }
}