package com.onandoff.onandoff_android.presentation.home.posting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onandoff.onandoff_android.HomeFragment
import com.onandoff.onandoff_android.databinding.FragmentPostingCategoryBinding
import com.onandoff.onandoff_android.databinding.FragmentPostingOptionBinding

class PostingOptionFragment : Fragment() {
    private var _binding : FragmentPostingOptionBinding? = null
    private val binding
        get() = _binding!!

    // postingId 가져와야 함.
    val postingId = arguments?.getInt("postingId")

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
        }
        binding.btnDelete.setOnClickListener{
            // 글 삭제
            intentPostingActivity(1)
        }
        binding.btnModify.setOnClickListener {
            // 카테고리 선택(Sports)
            intentPostingActivity(2)
        }
    }

    private fun intentPostingActivity(option: Int) {
        var intent : Intent? = null
        when (option) {
            1 -> {intent = Intent(activity, HomeFragment::class.java)} // 수정해야 함 : 글 삭제 후 전환될 페이지
            2 -> {
                intent = Intent(activity, PostingModifyActivity::class.java)
                intent.apply {
                    this.putExtra("postingId", postingId)
                }
            }
            else -> print("default")
        }

        startActivity(intent)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}