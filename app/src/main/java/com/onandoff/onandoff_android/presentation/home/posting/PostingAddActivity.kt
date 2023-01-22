package com.onandoff.onandoff_android.presentation.home.posting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.ActivityPostingAddBinding

class PostingAddActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPostingAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostingAddBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.btnPostingAdd.setOnClickListener{
            // 게시물 추가
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
            val dialog = BottomSheetDialog(this)
            dialog.setContentView(postingCategoryView)
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()
        }
    }
}