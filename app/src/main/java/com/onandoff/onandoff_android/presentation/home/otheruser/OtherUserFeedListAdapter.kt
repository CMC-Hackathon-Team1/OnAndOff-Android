package com.onandoff.onandoff_android.presentation.home.otheruser

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.FeedResponse
import com.onandoff.onandoff_android.data.model.FeedResponseData
import com.onandoff.onandoff_android.data.model.FeedSimpleData
import com.onandoff.onandoff_android.data.model.LookAroundFeedData
import com.onandoff.onandoff_android.databinding.ItemMypageUserfeedBinding
import com.onandoff.onandoff_android.presentation.home.posting.PostingImageAdapter
import com.onandoff.onandoff_android.presentation.look.BottomSheetLookAroundFeedOptionMenu
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("NotifyDataSetChanged")
class OtherUserFeedListAdapter(private var feedList : List<FeedResponseData>) : RecyclerView.Adapter<OtherUserFeedListAdapter.OtherUserFeedListViewHolder>() {

    private lateinit var itemClickListener : OnItemClickListener
    private lateinit var imageAdapter: PostingImageAdapter
    inner class OtherUserFeedListViewHolder(val binding: ItemMypageUserfeedBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(write: FeedResponseData){
            imageAdapter = PostingImageAdapter()
            binding.ivMypageRvItemPostImg.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
            binding.ivMypageRvItemPostImg.adapter = imageAdapter

            if(write.feedImgList.isEmpty()) {
                binding.ivMypageRvItemPostImg.visibility = View.GONE
            } else {
                imageAdapter.setItems(write.feedImgList)
                imageAdapter.notifyDataSetChanged()
            }

            binding.tvMypageRvItemPostText.text = write.feedContent
            binding.tvMypageRvItemDate.text = write.createdAt.split("T")[0].replace("-","/")
            if(write.isLike) {
                binding.ivMypageRvItemLike.setImageResource(R.drawable.ic_heart_full)
                binding.tvMypageRvItemLike.text = write.likeNum.toString()
                binding.tvMypageRvItemLike.visibility = View.VISIBLE
            }
            else {
                binding.ivMypageRvItemLike.setImageResource(R.drawable.ic_heart_mono)
                binding.tvMypageRvItemLike.visibility = View.GONE
            }
            binding.ivMypageRvItemLike.setOnClickListener {
                val profileId = prefs.getSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID, -1)
                val data = FeedSimpleData(profileId, write.feedId)
                RetrofitClient.getClient()?.create(FeedInterface::class.java)
                    ?.likeFeedResponse(data)?.enqueue(object : Callback<FeedResponse> {
                        override fun onResponse(
                            call: Call<FeedResponse>,
                            response: Response<FeedResponse>
                        ) {
                            if(response.body()?.message  == "Like") {
                                binding.ivMypageRvItemLike.setImageResource(R.drawable.ic_heart_full)
                                binding.tvMypageRvItemLike.text = write.likeNum.toString()
                                binding.tvMypageRvItemLike.visibility = View.VISIBLE
                            }
                            else {
                                binding.ivMypageRvItemLike.setImageResource(R.drawable.ic_heart_mono)
                                binding.tvMypageRvItemLike.visibility = View.GONE
                            }
                        }

                        override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
            }

            binding.ivMypageRvItemMore.setOnClickListener {
                itemClickListener.onClick(itemView, write.feedId)
            }
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


    interface OnItemClickListener {
        fun onClick(v: View, feedId: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setItems(item: List<FeedResponseData>) {
        feedList = feedList + item
        notifyDataSetChanged()
    }

    fun removeItems() {
        feedList = emptyList()
        notifyDataSetChanged()
    }
}