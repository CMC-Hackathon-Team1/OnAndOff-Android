package com.onandoff.onandoff_android.presentation.mypage

import android.content.Context
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.user.ProfileInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.*
import com.onandoff.onandoff_android.data.request.LikeRequest
import com.onandoff.onandoff_android.databinding.ItemMypageUserfeedBinding
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageRVAdapter(private val writeList : ArrayList<FeedResponseData>,private val context: Context):RecyclerView.Adapter<MypageRVAdapter.MypageViewHolder>() {
    inner class MypageViewHolder(val binding:ItemMypageUserfeedBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(write: FeedResponseData){
//                binding.feedListItem = write
            binding.tvMypageRvItemPostText.text = write.feedContent
            binding.tvMypageRvItemDate.text = write.createdAt.substring(0,4)+'/'+write.createdAt.substring(5,7)+'/'+write.createdAt.substring(8,10)
            binding.tvMypageRvItemLike.text = write.likeNum.toString()
            if(write.feedImgList.isNotEmpty()){
                Glide.with(context).load(write.feedImgList[0]).into(binding.ivMypageRvItemPostImg)
            }else{
                binding.ivMypageRvItemPostImg.visibility = View.GONE
            }
            binding.ivMypageRvItemMore.setOnClickListener{
                // TODO: edit intent
            }
            var isLike = write.isLike
            if(write.isLike){
                binding.ivMypageRvItemLike.setImageResource(R.drawable.ic_heart_full)
                isLike = true
            }else{
                binding.ivMypageRvItemLike.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                isLike = false
            }
            binding.ivMypageRvItemLike.setOnClickListener{
                if(isLike){
                    binding.ivMypageRvItemLike.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    if(isLike == write.isLike){
                        binding.tvMypageRvItemLike.text = write.likeNum.toString()
                    }else{
                        binding.tvMypageRvItemLike.text = (write.likeNum-1).toString()

                    }
                    isLike = false
                }else{
                    binding.ivMypageRvItemLike.setImageResource(R.drawable.ic_heart_full)
                    if(isLike == write.isLike){
                        binding.tvMypageRvItemLike.text = write.likeNum.toString()
                    }else{
                        binding.tvMypageRvItemLike.text = (write.likeNum+1).toString()

                    }
                    isLike = true
                }
                postLike(write.feedId)
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
    fun postLike(feedId:Int){
        val feedInterface: FeedInterface? = RetrofitClient.getClient()?.create(
            FeedInterface::class.java)
        val profileId = SharePreference.prefs.getSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID,0)
        //날을 어떻게 넣을지 고민해봐야될듯
        val call = feedInterface?.likeFeedResponse(FeedSimpleData(profileId,feedId))
        call?.enqueue(object: Callback<FeedResponse> {
            override fun onResponse(
                call: Call<FeedResponse>,
                response: Response<FeedResponse>
            ){
                Log.d("mypage","자신의 글에 좋아요를 남겼습니다")
            }
            override fun onFailure(call: Call<FeedResponse>, t: Throwable){
                Log.d("mypage","$t")

            }
        })
    }
}