package com.onandoff.onandoff_android.presentation.home.posting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.onandoff.onandoff_android.databinding.FragmentPostingOptionBinding


class PostingOptionFragment(val itemClick: (Int) -> Unit) : BottomSheetDialogFragment() {
    private var _binding : FragmentPostingOptionBinding? = null
    private val binding
        get() = _binding!!

    // feedId 가져와야 함.
    val feedId = arguments?.getInt("feedId")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostingOptionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnPostingOptionOut.setOnClickListener{
            // 창닫기
            dialog?.dismiss()
        }
        binding.btnDelete.setOnClickListener{
            // 글 삭제
            itemClick(0)
            dialog?.dismiss()
        }
        binding.btnModify.setOnClickListener {
            // 글 수정
            itemClick(1)
            dialog?.dismiss()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
