package com.onandoff.onandoff_android.presentation.home.setting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.FragmentAccountBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.home.HomeFragment
import com.onandoff.onandoff_android.presentation.home.SettingFragment

class AccountFragment :Fragment(){
    private lateinit var binding:FragmentAccountBinding
    lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        binding.ivBackArrow.setOnClickListener{
            mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_main, SettingFragment())
                .commit()
        }
        return binding.root
    }
}