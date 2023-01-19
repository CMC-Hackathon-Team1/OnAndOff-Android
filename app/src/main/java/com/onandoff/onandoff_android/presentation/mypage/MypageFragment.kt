package com.onandoff.onandoff_android.presentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.onandoff.onandoff_android.databinding.FragmentMypageBinding

class MypageFragment: Fragment(){

    private lateinit var binding : FragmentMypageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(layoutInflater)
        return binding.root
    }

}