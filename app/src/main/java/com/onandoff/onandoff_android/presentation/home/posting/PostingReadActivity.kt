package com.onandoff.onandoff_android.presentation.home.posting

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.FeedDeleteData
import com.onandoff.onandoff_android.data.model.FeedReadData
import com.onandoff.onandoff_android.data.model.FeedResponse
import com.onandoff.onandoff_android.databinding.ActivityPostingReadBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class PostingReadActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostingReadBinding
    private val feedInterface : FeedInterface? = RetrofitClient.getClient()?.create(FeedInterface::class.java)
    var profileId by Delegates.notNull<Int>()
    var feedId by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostingReadBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        profileId = intent.getIntExtra("profileId",0)
        feedId = intent.getIntExtra("feedId",0)
        readPost(profileId, feedId)
        init()
    }

    private fun init() {
        binding.btnPostingReadOut.setOnClickListener {
            //뒤로가기 기능
            finish()
        }
        binding.btnPostingReadDots.setOnClickListener {
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
        }
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
                            if(response.body()!!.isLike) {
                                binding.posting.imageLike.setImageResource(R.drawable.ic_heart_full)
                            } else {
                                binding.posting.imageLike.setImageResource(R.drawable.ic_heart_mono)
                            }
                            val hashTagList = response.body()!!.hashTagList
                            var tagText = ""
                            for (tag in hashTagList) {
                                tagText = "#$tag $tagText "
                            }
                            binding.posting.textHashtag.text = tagText
                            binding.posting.textWriteDate.text = response.body()!!.createdAt
                            binding.posting.textWriter.text = response.body()!!.personaName + " " + response.body()!!.profileName
                            if(response.body()!!.feedImgList.isEmpty()) {
                                binding.posting.imagePhoto.visibility = View.GONE
                            } else {
                                Glide.with(binding.root.context)
                                    .load(response.body()!!.feedImgList[0])
                                    .into(binding.posting.imagePhoto)
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
        val feedDeleteData = FeedDeleteData(profileId, feedId)

        val call = feedInterface?.deleteFeedResponse(feedDeleteData)
        call?.enqueue(object : Callback<FeedResponse>{
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if(response.code() == 200)
                    Toast.makeText(this@PostingReadActivity, "해당 게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
            }

            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                //TODO("Not yet implemented")
            }

        })
    }
}