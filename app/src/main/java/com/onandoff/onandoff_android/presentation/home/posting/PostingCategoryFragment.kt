package com.onandoff.onandoff_android.presentation.home.posting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.onandoff.onandoff_android.databinding.FragmentPostingCategoryBinding

class PostingCategoryFragment(val itemClick: (Int) -> Unit) : BottomSheetDialogFragment() {
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
            dialog?.dismiss()
        }
        binding.btnArt.setOnClickListener{
            // 카테고리 선택(문화/예슬)
            itemClick(0)
            dialog?.dismiss()
        }
        binding.btnSports.setOnClickListener {
            // 카테고리 선택(Sports)
            itemClick(1)
            dialog?.dismiss()
        }
        binding.btnSelf.setOnClickListener {
            // 카테고리 선택(자기계발)
            itemClick(2)
            dialog?.dismiss()
        }
        binding.btnEtc.setOnClickListener {
            // 카테고리 선택(기타)
            itemClick(3)
            dialog?.dismiss()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}