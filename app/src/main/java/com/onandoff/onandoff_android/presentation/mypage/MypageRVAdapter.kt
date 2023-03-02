package com.onandoff.onandoff_android.presentation.mypage

import android.content.Context
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.data.model.FeedResponseData
import com.onandoff.onandoff_android.data.model.MyPosting
import com.onandoff.onandoff_android.databinding.ItemMypageUserfeedBinding

class MypageRVAdapter(private val writeList : ArrayList<FeedResponseData>,private val context: Context):RecyclerView.Adapter<MypageRVAdapter.MypageViewHolder>() {
    inner class MypageViewHolder(val binding:ItemMypageUserfeedBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(write: FeedResponseData){
            binding.tvMypageRvItemPostText.text = write.feedContent
//            binding.tvMypageRvItemLike.text = write.
            binding.tvMypageRvItemDate.text = write.createdAt
            Glide.with(context).asBitmap().load(Base64.decode(write.feedImgList[0],Base64.DEFAULT)).into(binding.ivMypageRvItemPostImg)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MypageViewHolder {
        val viewBinding = ItemMypageUserfeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MypageViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: MypageViewHolder, position: Int) {
        Log.d("RecyclerView","${writeList.size}")
        holder.bind(writeList[position])
    }

    override fun getItemCount(): Int = writeList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }
}