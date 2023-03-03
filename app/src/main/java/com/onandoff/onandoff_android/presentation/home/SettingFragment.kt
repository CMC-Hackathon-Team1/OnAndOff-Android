package com.onandoff.onandoff_android.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.onandoff.onandoff_android.databinding.FragmentProfileEditBinding
import com.onandoff.onandoff_android.databinding.FragmentSettingBinding

class SettingFragment:Fragment() {
    private lateinit var binding:FragmentSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        binding.layoutSettingAccount.setOnClickListener {


        }
        binding.layoutSettingPrivate.setOnClickListener {

        }
        binding.layoutSettingAlarm.setOnClickListener {

        }
        binding.layoutSettingFeedback.setOnClickListener {

        }
        binding.layoutSettingPolicy.setOnClickListener {

        }
        binding.layoutSettingLogout.setOnClickListener {

        }
        return binding.root
    }
}