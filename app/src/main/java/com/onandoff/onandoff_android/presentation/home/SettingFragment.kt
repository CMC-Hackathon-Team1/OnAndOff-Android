package com.onandoff.onandoff_android.presentation.home

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.DialogLogoutBinding
import com.onandoff.onandoff_android.databinding.DialogProfileDeleteBinding
import com.onandoff.onandoff_android.databinding.FragmentProfileEditBinding
import com.onandoff.onandoff_android.databinding.FragmentSettingBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.home.setting.AccountFragment
import com.onandoff.onandoff_android.presentation.home.setting.PolicyFragment
import com.onandoff.onandoff_android.presentation.mypage.MypageFragment

class SettingFragment:Fragment() {
    private lateinit var binding:FragmentSettingBinding
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
        binding = FragmentSettingBinding.inflate(layoutInflater)
        binding.layoutSettingAccount.setOnClickListener {
            mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_main, AccountFragment())
                .commit()

        }
        binding.layoutSettingAlarm.setOnClickListener {

        }
        binding.layoutSettingFeedback.setOnClickListener {

        }
        binding.layoutSettingPolicy.setOnClickListener {
            mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_main, PolicyFragment())
                .commit()
        }
        binding.layoutSettingLogout.setOnClickListener {
            val dialog = Dialog(mainActivity)
            val dialogView = DialogLogoutBinding.inflate(LayoutInflater.from(mainActivity))
            dialog.setContentView(dialogView.root)
            val params : WindowManager.LayoutParams? = dialog.window?.attributes;
            params?.width = WindowManager.LayoutParams.MATCH_PARENT
            params?.height = WindowManager.LayoutParams.WRAP_CONTENT
            if (params != null) {
                dialog.window?.setLayout(params.width,params.height)
            }
            dialog.show()
            dialogView.btnYes.setOnClickListener{
               // TODO : 로그아웃 api 연결
            }
            dialogView.btnNo.setOnClickListener{
                dialog.dismiss()
            }

        }
        return binding.root
    }
}