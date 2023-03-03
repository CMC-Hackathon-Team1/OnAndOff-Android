package com.onandoff.onandoff_android.presentation.home.posting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.feed.FeedInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.FeedData
import com.onandoff.onandoff_android.data.model.FeedReadData
import com.onandoff.onandoff_android.data.model.FeedResponse
import com.onandoff.onandoff_android.databinding.FragmentPostingReadBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostingReadFragment() : BottomSheetDialogFragment() {
    private var _binding : FragmentPostingReadBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostingReadBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readPost()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun readPost(){
        val profileId = 27
        val feedId = 3104

        val feedInterface : FeedInterface? = RetrofitClient.getClient()?.create(FeedInterface::class.java)
        val call = feedInterface?.readFeedResponse(feedId, profileId)
        call?.enqueue(object : Callback<FeedReadData> {
            override fun onResponse(call: Call<FeedReadData>, response: Response<FeedReadData>) {
                when(response.code()) {
                    200 -> {
                        Log.d("readFeed", "onResponse: Success + ${response.body().toString()}")
                        if(response.body()!=null) {
                            binding.posting.textContent.text = response.body()!!.feedContent
                            if(response.body()!!.isLike) {
                                binding.posting.imageLike.setImageResource(R.drawable.ic_heart_full)
                            } else {
                                binding.posting.imageLike.setImageResource(R.drawable.ic_heart_mono)
                            }
                            binding.posting.textWriteDate.text = response.body()!!.createdAt
                            binding.posting.textWriter.text = response.body()!!.personaName + response.body()!!.profileName
//                            if(response.body()!!.feedImgList.isNullOrEmpty()) {
////                                binding.posting.imagePhoto.visibility = View.GONE
//                            } else {
//                                Glide.with(binding.root.context)
//                                    .load(response.body()!!.feedImgList[0])
//                                    .into(binding.posting.imagePhoto)
//                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<FeedReadData>, t: Throwable) {
                Log.d("TAG", "onFailure: addFeed error")
            }

        })
    }
}