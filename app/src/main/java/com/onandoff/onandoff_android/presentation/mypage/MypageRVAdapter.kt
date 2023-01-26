package com.onandoff.onandoff_android.presentation.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onandoff.onandoff_android.data.model.MyPosting
import com.onandoff.onandoff_android.databinding.ItemMypageUserfeedBinding

class MypageRVAdapter(private val writeList : ArrayList<MyPosting>):RecyclerView.Adapter<MypageRVAdapter.MypageViewHolder>() {
    inner class MypageViewHolder(val binding:ItemMypageUserfeedBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(write: MyPosting){
            binding.tvMypageRvItemPostText.text = write.content
            binding.tvMypageRvItemLike.text = write.likeCount
            binding.tvMypageRvItemDate.text = write.createdAt

        }


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MypageViewHolder {
        val viewBinding = ItemMypageUserfeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MypageViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: MypageViewHolder, position: Int) {


    }

    override fun getItemCount(): Int = writeList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }
}