package com.onandoff.onandoff_android.presentation.home.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.FragmentPolicyBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.home.SettingFragment
import com.onandoff.onandoff_android.presentation.mypage.MypageFragment
import com.onandoff.onandoff_android.presentation.splash.PrivatePolicyActivity
import com.onandoff.onandoff_android.presentation.splash.ServicePolicyActivity

class PolicyFragment:Fragment() {
    private lateinit var binding:FragmentPolicyBinding
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
        binding = FragmentPolicyBinding.inflate(layoutInflater)
        binding.ivBackArrow.setOnClickListener{
            mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_main, SettingFragment())
                .commit()
        }
        binding.ivPrivatePolicyMore.setOnClickListener{
            val intent = Intent(mainActivity, PrivatePolicyActivity::class.java)
            startActivity(intent)
        }
        binding.ivServicePolicyMore.setOnClickListener{
            val intent = Intent(mainActivity, ServicePolicyActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}