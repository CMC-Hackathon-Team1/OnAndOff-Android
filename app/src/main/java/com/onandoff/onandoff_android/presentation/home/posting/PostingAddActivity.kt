package com.onandoff.onandoff_android.presentation.home.posting

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.FeedData
import com.onandoff.onandoff_android.data.model.FeedResponse
import com.onandoff.onandoff_android.databinding.ActivityPostingAddBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.Objects

class PostingAddActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostingAddBinding
    var categoryId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostingAddBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.btnPostingAdd.setOnClickListener{
            // 게시물 추가
            addPosting()
        }
        binding.btnCamera.setOnClickListener{
            // 사진 추가
        }
        binding.btnBefore.setOnClickListener {
            // 이전으로
            val builder = AlertDialog.Builder(this)
            builder.setMessage("글 쓰기를 취소하시겠습니까?")
                .setPositiveButton("네") { _, _ -> finish()}
                .setNegativeButton("아니오") { _, _ ->}
            builder.show()
        }
        binding.btnCategory.setOnClickListener {
            // 카테고리 선택 fragment 띄우기
            val bottomPostingCategoryFragment = PostingCategoryFragment{
                when (it) {
                    0 -> binding.textCategory.text = "문화 및 예술"
                    1 -> binding.textCategory.text = "스포츠"
                    2 -> binding.textCategory.text = "자기계발"
                    3 -> binding.textCategory.text = "기타"
                }
                categoryId = it
            }
            bottomPostingCategoryFragment.show(supportFragmentManager,bottomPostingCategoryFragment.tag)
        }
    }


    /**
     *  val profiledId: Int,
        val categoryId: Int,
        val hashTagList: List<String>,
        val content: String,
        val isSecret: String
     */
    private fun addPosting(){
        val hashTag = binding.textHashtag.text.toString()
        val hashTagList = hashTag.split(" #", " ", "#")

        val content = binding.textContent.text.toString()
        val isSecret = when(binding.checkboxSecret.isChecked) {
            false -> "PUBLIC"
            true -> "PRIVATE"
        }

        val data = FeedData(27, categoryId, hashTagList, content, isSecret)

        val feedInterface : FeedInterface? = RetrofitClient.getClient()?.create(FeedInterface::class.java)
        val call = feedInterface?.addFeedResponse(data)
        call?.enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                when(response.code()) {
                    200 -> {
                        Log.d("addFeed", "onResponse: Success + ${response.body().toString()}")
                    }
                }
            }

            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                Log.d("TAG", "onFailure: addFeed error")
            }

        })
    }
}