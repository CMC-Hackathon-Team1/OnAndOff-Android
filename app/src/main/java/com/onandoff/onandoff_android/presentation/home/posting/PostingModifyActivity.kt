package com.onandoff.onandoff_android.presentation.home.posting

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.FeedData
import com.onandoff.onandoff_android.data.model.FeedReadData
import com.onandoff.onandoff_android.data.model.FeedResponse
import com.onandoff.onandoff_android.databinding.ActivityPostingModifyBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostingModifyActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostingModifyBinding
    private val feedInterface : FeedInterface? = RetrofitClient.getClient()?.create(FeedInterface::class.java)
    var categoryId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostingModifyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val feedId = intent.getIntExtra("feedId",-1)
        val profileId = intent.getIntExtra("profileId", -1)

        // 기존 게시물 내용 불러오기
        getPostingByFeedId(profileId,feedId)

        binding.btnPostingModify.setOnClickListener{
            val hashTag = binding.textHashtag.text.toString()
            var hashTagList = hashTag.split(" ", "#")
            hashTagList = hashTagList.filter { it.isNotEmpty() }

            val content = binding.textContent.text.toString()
            val isSecret = when(binding.checkboxSecret.isChecked) {
                false -> "PUBLIC"
                true -> "PRIVATE"
            }

            if (profileId == -1) {
                finish()
            } else if (hashTagList.isEmpty()) {
                Toast.makeText(this@PostingModifyActivity, "해쉬태그를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (content.isEmpty() || content == "") {
                Toast.makeText(this@PostingModifyActivity, "피드의 내용을 입력해주세요.", Toast.LENGTH_SHORT)
                    .show()
            } else if (categoryId == 0) {
                Toast.makeText(this@PostingModifyActivity, "피드의 카테고리를 선택해주세요.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // 게시물 추가
                modifyPosting(profileId, feedId, hashTagList, content, isSecret)
            }
        }
        binding.btnCamera.setOnClickListener{
            // 사진 추가
        }
        binding.btnBefore.setOnClickListener {
            // 이전으로
            val builder = AlertDialog.Builder(this)
            builder.setMessage("글 수정을 취소하시겠습니까?")
                .setPositiveButton("네") { _, _ -> finish()}
                .setNegativeButton("아니오") { _, _ ->}
            builder.show()
        }
        binding.categoryLayout.setOnClickListener {
            // 카테고리 선택 fragment 띄우기
            val bottomPostingCategoryFragment = PostingCategoryFragment{
                when (it) {
                    1 -> binding.textCategory.text = "문화 및 예술"
                    2 -> binding.textCategory.text = "스포츠"
                    3 -> binding.textCategory.text = "자기계발"
                    4 -> binding.textCategory.text = "기타"
                }
                categoryId = it
            }
            bottomPostingCategoryFragment.show(supportFragmentManager,bottomPostingCategoryFragment.tag)
        }
    }

    private fun getPostingByFeedId(profileId: Int,feedId: Int) {
        val call = feedInterface?.readFeedResponse(feedId, profileId)
        call?.enqueue(object : Callback<FeedReadData> {
            override fun onResponse(call: Call<FeedReadData>, response: Response<FeedReadData>) {
                when(response.code()) {
                    200 -> {
                        Log.d("readFeed", "onResponse: Success + ${response.body()!!.hashTagList}")
                        if(response.body()!=null) {
                            binding.textContent.setText(response.body()!!.feedContent)
                            val hashTagList = response.body()!!.hashTagList
                            var tagText = ""
                            for (tag in hashTagList) {
                                tagText = "#$tag $tagText"
                            }
                            binding.textHashtag.setText(tagText)
                            when(response.body()!!.categoryId) {
                                0 -> binding.textCategory.text = "카테고리"
                                1 -> binding.textCategory.text = "문화 및 예술"
                                2 -> binding.textCategory.text = "스포츠"
                                3 -> binding.textCategory.text = "자기계발"
                                4 -> binding.textCategory.text = "기타"
                            }
                            categoryId = response.body()!!.categoryId
                            finish()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<FeedReadData>, t: Throwable) {
                Log.d("TAG", "onFailure: addFeed error")
            }
        })
    }

    private fun modifyPosting(profileId: Int,feedId: Int, hashTagList: List<String>, content:String, isSecret: String){
        val data = FeedData(profileId, feedId, categoryId, hashTagList, content, isSecret)

        val call = feedInterface?.updateFeedResponse(data)
        call?.enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                when(response.code()) {
                    200 -> {
                        Log.d("updateFeed", "onResponse: ${response.body().toString()}")
                    }
                }
            }

            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                Log.d("TAG", "onFailure: addFeed error")
            }
        })
    }
}