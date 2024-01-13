package com.onandoff.onandoff_android.presentation.mypage

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.*
import com.onandoff.onandoff_android.databinding.ItemMypageUserfeedBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.home.HomeFragment
import com.onandoff.onandoff_android.presentation.home.posting.PostingImageAdapter
import com.onandoff.onandoff_android.presentation.home.posting.PostingModifyActivity
import com.onandoff.onandoff_android.presentation.home.posting.PostingOptionFragment
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID
import com.onandoff.onandoff_android.util.APIPreferences.SHARED_PREFERENCE_NAME_USERID
import com.onandoff.onandoff_android.util.SharePreference
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageRVAdapter(private val writeList : ArrayList<FeedResponseData>,private val context: Context,fragmentManager: FragmentManager):RecyclerView.Adapter<MypageRVAdapter.MypageViewHolder>() {
    var feedId:Int = 0
    var profileId:Int = prefs.getSharedPreference(SHARED_PREFERENCE_NAME_PROFILEID,0)

    var fragmentManager:FragmentManager = fragmentManager
    private val feedInterface : FeedInterface? = RetrofitClient.getClient()?.create(FeedInterface::class.java)
    private lateinit var imageAdapter: PostingImageAdapter
    inner class MypageViewHolder(val binding:ItemMypageUserfeedBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(write: FeedResponseData) {
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
            binding.ivMypageRvItemMore.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("feedId", write.feedId)
                feedId = write.feedId
                val bottomPostingOptionFragment = PostingOptionFragment {
                    when (it) {
                        1 -> {
                            var intent:Intent = Intent(context, PostingModifyActivity::class.java)
                            intent.putExtra("profileId",profileId)
                            intent.putExtra("feedId", feedId)
                            startActivity(context,intent,bundle)
                        }
                        0 -> {
                            showDeleteDialog()
                        }
                    }
                }
                try {
                    bottomPostingOptionFragment.show(
                        fragmentManager,
                        bottomPostingOptionFragment.tag
                    )
                } catch (e: Exception) {
                    Log.d("hh","${e}")
                }

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
    private fun showDeleteDialog() {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Feed Delete")
        dialog.setMessage("이 글을 정말로 삭제하시겠습니까?")

        val dialogListener = DialogInterface.OnClickListener { _, p1 ->
            when(p1){
                DialogInterface.BUTTON_POSITIVE -> {
                    deleteFeed()
                }
                DialogInterface.BUTTON_NEGATIVE -> Log.d("Dialog","다이얼로그 닫기!")
            }
        }

        dialog.setPositiveButton("삭제",dialogListener)
        dialog.setNegativeButton("취소",dialogListener)
        dialog.show()
    }

    private fun deleteFeed(){
        val feedSimpleData = FeedSimpleData(profileId, feedId)
        val call = feedInterface?.deleteFeedResponse(feedSimpleData)
        call?.enqueue(object : Callback<FeedResponse>{
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                when(response.code()) {
                    200 -> {
                        Toast.makeText(context, "해당 게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()


                    }
                    else -> {
                    }
                }

            }

            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                // TODO("Not yet implemented")
            }

        })
    }
}
