package com.onandoff.onandoff_android.presentation.home.posting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.ActivityPostingAddBinding
import com.onandoff.onandoff_android.databinding.ActivityPostingModifyBinding
import com.onandoff.onandoff_android.databinding.FragmentPostingCategoryBinding

class PostingCategoryFragment : Fragment() {
    private var _binding : FragmentPostingCategoryBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostingCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnPostingCategoryOut.setOnClickListener{
            // 창닫기
        }
        binding.btnArt.setOnClickListener{
            // 카테고리 선택(문화/예슬)
            intentPostingActivity("art")
        }
        binding.btnSports.setOnClickListener {
            // 카테고리 선택(Sports)
            intentPostingActivity("sports")
        }
        binding.btnSelf.setOnClickListener {
            // 카테고리 선택(자기계발)
            intentPostingActivity("self")
        }
        binding.btnEtc.setOnClickListener {
            // 카테고리 선택(기타)
            intentPostingActivity("etc")
        }
    }

    private fun intentPostingActivity(category: String) {
        val intent = Intent(activity, PostingAddActivity::class.java)
        intent.apply {
            this.putExtra("category", category)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}