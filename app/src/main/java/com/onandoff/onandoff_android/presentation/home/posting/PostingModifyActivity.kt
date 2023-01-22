package com.onandoff.onandoff_android.presentation.home.posting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.ActivityPostingModifyBinding

class PostingModifyActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostingModifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostingModifyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val postingId = intent.getStringExtra("postingId")

        // 기존 게시물 내용 불러오기
        getPostingByPostingId(postingId)

        binding.btnPostingModify.setOnClickListener{
            // 게시물 수정
        }
        binding.btnCamera.setOnClickListener{
            // 사진 추가
        }
        binding.btnBefore.setOnClickListener {
            // 이전으로
        }
        binding.btnCategory.setOnClickListener {
            // 카테고리 선택 fragment 띄우기
            val postingCategoryView = layoutInflater.inflate(R.layout.fragment_posting_category, null)
            val bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(postingCategoryView)
            bottomSheetDialog.show()
        }
    }

    private fun getPostingByPostingId(postingId: String?) {
        // 기존 게시물 내용 불러오기
    }



}