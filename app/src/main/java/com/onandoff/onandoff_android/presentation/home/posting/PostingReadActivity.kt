package com.onandoff.onandoff_android.presentation.home.posting

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.FeedSimpleData
import com.onandoff.onandoff_android.data.model.FeedReadData
import com.onandoff.onandoff_android.data.model.FeedResponse
import com.onandoff.onandoff_android.data.model.LikeFollowResponse
import com.onandoff.onandoff_android.data.request.FollowRequest
import com.onandoff.onandoff_android.databinding.ActivityPostingReadBinding
import com.onandoff.onandoff_android.presentation.look.BottomSheetLookAroundFeedOptionMenu
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class PostingReadActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostingReadBinding
    private val feedInterface : FeedInterface? = RetrofitClient.getClient()?.create(FeedInterface::class.java)
    var profileId by Delegates.notNull<Int>()
    var otherUserId by Delegates.notNull<Int>()
    var feedId by Delegates.notNull<Int>()
    var likeImg by Delegates.notNull<Boolean>()
    var followImg by Delegates.notNull<Boolean>()
    private lateinit var imageAdapter: PostingImageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostingReadBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        profileId = intent.getIntExtra("profileId",-1)
        otherUserId = intent.getIntExtra("otherUserId",-1)
        feedId = intent.getIntExtra("feedId",-1)
        if(profileId == -1 || feedId == -1) finish()
        if (otherUserId == -1) {
            binding.posting.imageFollow.visibility = View.GONE
        } else {
            binding.posting.imageFollow.visibility = View.VISIBLE
        }
        readPost(profileId, feedId)
        init()
    }

    private fun init() {
        imageAdapter = PostingImageAdapter()
        binding.posting.imagePhotoList.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
        binding.posting.imagePhotoList.adapter = imageAdapter

        binding.btnPostingReadOut.setOnClickListener {
            //뒤로가기 기능
            finish()
        }
        binding.btnPostingReadDots.setOnClickListener {
            if(otherUserId == -1) {
                val bottomPostingOptionFragment = PostingOptionFragment{
                    when(it) {
                        1 -> {
                            val intent = Intent(this, PostingModifyActivity::class.java)
                            intent.putExtra("profileId", profileId)
                            intent.putExtra("feedId", feedId)
                            startActivity(intent)
                        }
                        0 -> {
                            showDeleteDialog()
                        }
                    }
                }
                bottomPostingOptionFragment.show(supportFragmentManager, bottomPostingOptionFragment.tag)
            } else {
                val bottomSheetDialogFragment =
                    BottomSheetLookAroundFeedOptionMenu.newInstance(feedId = feedId)
                bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)
            }
        }

        binding.posting.imageLike.setOnClickListener {
            clickLike(profileId, feedId)

            if(!likeImg) {
                binding.posting.imageLike.setImageResource(R.drawable.ic_heart_full)
                binding.posting.textLikeCount.visibility = View.VISIBLE

            } else {
                binding.posting.imageLike.setImageResource(R.drawable.ic_heart_mono)
                binding.posting.textLikeCount.visibility = View.INVISIBLE
            }
        }

        setupFollowStatus()

        binding.posting.imageFollow.setOnClickListener {
            clickFollow(profileId, otherUserId)

            if(!followImg) {
                binding.posting.imageFollow.setImageResource(R.drawable.ic_is_following)

            } else {
                binding.posting.imageFollow.setImageResource(R.drawable.ic_not_following)
            }
        }
    }

    private fun setupFollowStatus() {
        val request = FollowRequest(profileId, otherUserId)
        val call = feedInterface?.followStatusResponse(request)
        call?.enqueue(object : Callback<LikeFollowResponse> {
            override fun onResponse(
                call: Call<LikeFollowResponse>, response: Response<LikeFollowResponse>
            ) {
                followImg = if (response.body()?.message == "Follow") {
                    binding.posting.imageFollow.setImageResource(R.drawable.ic_is_following)
                    true
                } else {
                    binding.posting.imageFollow.setImageResource(R.drawable.ic_not_following)
                    false
                }
            }

            override fun onFailure(call: Call<LikeFollowResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun clickLike(profileId: Int, feedId: Int) {
        val feedSimpleData = FeedSimpleData(profileId, feedId)
        val call = feedInterface?.likeFeedResponse(feedSimpleData)
        call?.enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if (response.body() != null) {
                    likeImg = response.body()!!.message == "Like"
                }
            }

            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun clickFollow(profileId: Int, otherUserId: Int) {
        val feedSimpleData = FollowRequest(profileId, otherUserId)
        val call = feedInterface?.followResponse(feedSimpleData)
        call?.enqueue(object : Callback<LikeFollowResponse> {
            override fun onResponse(call: Call<LikeFollowResponse>, response: Response<LikeFollowResponse>) {
                if (response.body() != null) {
                    followImg = response.body()!!.message == "Follow"
                }
            }

            override fun onFailure(call: Call<LikeFollowResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun readPost(profileId: Int, feedId: Int){
        val call = feedInterface?.readFeedResponse(feedId, profileId)
        call?.enqueue(object : Callback<FeedReadData> {
            override fun onResponse(call: Call<FeedReadData>, response: Response<FeedReadData>) {
                when(response.code()) {
                    200 -> {
                        Log.d("readFeed", "onResponse: Success + ${response.body().toString()}")
                        if(response.body()!=null) {
                            binding.posting.textContent.text = response.body()!!.feedContent
                            likeImg = response.body()!!.isLike
                            if(response.body()!!.isLike) {
                                binding.posting.imageLike.setImageResource(R.drawable.ic_heart_full)
                                binding.posting.textLikeCount.visibility = View.VISIBLE
                            } else {
                                binding.posting.imageLike.setImageResource(R.drawable.ic_heart_mono)
                                binding.posting.textLikeCount.visibility = View.INVISIBLE
                            }
                            binding.posting.textLikeCount.text = response.body()!!.likeNum.toString()
                            val hashTagList = response.body()!!.hashTagList
                            var tagText = ""
                            if(!hashTagList.isNullOrEmpty()) {
                                for (tag in hashTagList) {
                                    tagText = "#$tag $tagText "
                                }
                            }

                            binding.posting.textHashtag.text = tagText
                            binding.posting.textWriteDate.text = response.body()!!.createdAt
                            binding.posting.textWriter.text = response.body()!!.personaName + " " + response.body()!!.profileName
                            if(response.body()!!.feedImgList.isEmpty()) {
                                binding.posting.imagePhotoList.visibility = View.GONE
                            } else {
                                imageAdapter.setItems(response.body()!!.feedImgList)
                                imageAdapter.notifyDataSetChanged()
                            }
                            if (response.body()!!.profileImg.isNotEmpty()) {
                                Glide.with(binding.root.context)
                                    .load(response.body()!!.profileImg)
                                    .into(binding.posting.imageProfile)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<FeedReadData>, t: Throwable) {
                Log.d("TAG", "onFailure: addFeed error")
            }

        })
    }
    private fun showDeleteDialog() {
        val dialog = AlertDialog.Builder(this)
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
                        Toast.makeText(this@PostingReadActivity, "해당 게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else -> {
                        finish()
                    }
                }

            }

            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                // TODO("Not yet implemented")
            }

        })
    }
}