package com.onandoff.onandoff_android.presentation.home.posting

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.FeedCategoryResponse
import com.onandoff.onandoff_android.databinding.FragmentPostingCategoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
@SuppressLint("NotifyDataSetChanged")
class PostingCategoryFragment(
    val itemClick: (Int) -> Unit
) : BottomSheetDialogFragment(){
    private var _binding : FragmentPostingCategoryBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
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

//        categoryAdapter = CategoryAdapter()
//        binding.categoryList.layoutManager = LinearLayoutManager(requireContext())
//        binding.categoryList.adapter = categoryAdapter
//        getCategoryList()
//
//        categoryAdapter.setItemClickListener(object : CategoryAdapter.OnItemClickListener {
//            override fun onClick(v: View, categoryId: Int) {
////                itemClick(categoryId)
//                Log.d("categoryId", "onClick: $categoryId")
//                dialog?.dismiss()
//            }
//        })

        binding.btnArt.setOnClickListener{
            // 카테고리 선택(문화/예슬)
            itemClick(1)
            dialog?.dismiss()
        }
        binding.btnSports.setOnClickListener {
            // 카테고리 선택(Sports)
            itemClick(2)
            dialog?.dismiss()
        }
        binding.btnSelf.setOnClickListener {
            // 카테고리 선택(자기계발)
            itemClick(3)
            dialog?.dismiss()
        }
        binding.btnEtc.setOnClickListener {
            // 카테고리 선택(기타)
            itemClick(4)
            dialog?.dismiss()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }



    private fun getCategoryList() {
        val call = RetrofitClient.getClient()?.create(FeedInterface::class.java)?.getFeedCategoryResponse()
        call?.enqueue(object : Callback<FeedCategoryResponse> {
            override fun onResponse(call: Call<FeedCategoryResponse>, response: Response<FeedCategoryResponse>) {
                if (!response.body()?.result.isNullOrEmpty()){
                    categoryAdapter.setItems(response.body()!!.result)
                    categoryAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<FeedCategoryResponse>, t: Throwable) {
                //TODO("Not yet implemented")
            }
        })
    }
}
