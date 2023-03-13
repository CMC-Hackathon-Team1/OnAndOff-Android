package com.onandoff.onandoff_android.presentation.mypage

import android.content.Context
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.data.model.FeedResponseData
import com.onandoff.onandoff_android.data.model.MyPosting
import com.onandoff.onandoff_android.databinding.ItemMypageUserfeedBinding
import com.onandoff.onandoff_android.presentation.home.posting.PostingImageAdapter

class MypageRVAdapter(private val writeList : ArrayList<FeedResponseData>,private val context: Context):RecyclerView.Adapter<MypageRVAdapter.MypageViewHolder>() {
    private lateinit var imageAdapter: PostingImageAdapter
    inner class MypageViewHolder(val binding:ItemMypageUserfeedBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(write: FeedResponseData){
//                binding.feedListItem = write
            imageAdapter = PostingImageAdapter()
            binding.ivMypageRvItemPostImg.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
            binding.ivMypageRvItemPostImg.adapter = imageAdapter
            binding.tvMypageRvItemPostText.text = write.feedContent
            binding.tvMypageRvItemDate.text = write.createdAt.substring(0,4)+'/'+write.createdAt.substring(5,7)+'/'+write.createdAt.substring(8,10)
            binding.tvMypageRvItemLike.text = write.likeNum.toString()
            if(write.feedImgList.isEmpty()) {
                binding.ivMypageRvItemPostImg.visibility = View.GONE
            } else {
                imageAdapter.setItems(write.feedImgList)
                imageAdapter.notifyDataSetChanged()
            }
            Log.d("feed","그려짐..?")
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