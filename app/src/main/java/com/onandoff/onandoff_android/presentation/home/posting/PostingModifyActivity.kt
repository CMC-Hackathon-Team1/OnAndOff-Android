package com.onandoff.onandoff_android.presentation.home.posting

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.onandoff.onandoff_android.databinding.ActivityPostingModifyBinding

class PostingModifyActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostingModifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostingModifyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val feedId = intent.getIntExtra("feedId",-1)

        // 기존 게시물 내용 불러오기
        getPostingByFeedId(feedId)

        binding.btnPostingModify.setOnClickListener{
            // 게시물 수정
            modifyPosting(feedId)
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
        binding.btnCategory.setOnClickListener {
            // 카테고리 선택 fragment 띄우기
            val bottomPostingCategoryFragment = PostingCategoryFragment{
                when (it) {
                    0 -> binding.posting.category = "문화 및 예술"
                    1 -> binding.posting.category = "스포츠"
                    2 -> binding.posting.category = "자기계발"
                    3 -> binding.posting.category = "기타"
                }
            }
            bottomPostingCategoryFragment.show(supportFragmentManager,bottomPostingCategoryFragment.tag)
        }
    }

    private fun getPostingByFeedId(feedId: Int) {
        // 기존 게시물 내용 불러오기
    }

    private fun modifyPosting(feedId: Int){
        // TODO : 게시물 수정 API 연결
    }



}