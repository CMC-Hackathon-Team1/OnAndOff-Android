package com.onandoff.onandoff_android.presentation.home

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.JsonElement
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.user.ProfileInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.FeedbackRequest
import com.onandoff.onandoff_android.data.model.ProfileResult
import com.onandoff.onandoff_android.databinding.DialogLogoutBinding
import com.onandoff.onandoff_android.databinding.DialogProfileDeleteBinding
import com.onandoff.onandoff_android.databinding.FragmentProfileEditBinding
import com.onandoff.onandoff_android.databinding.FragmentSettingBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.home.setting.AccountFragment
import com.onandoff.onandoff_android.presentation.home.setting.AlarmFragment
import com.onandoff.onandoff_android.presentation.home.setting.FeedbackFragment
import com.onandoff.onandoff_android.presentation.home.setting.PolicyFragment
import com.onandoff.onandoff_android.presentation.mypage.MypageFragment
import com.onandoff.onandoff_android.presentation.splash.SplashActivity
import com.onandoff.onandoff_android.util.APIPreferences.SHARED_PREFERENCE_NAME_JWT
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingFragment:Fragment() {
    private lateinit var binding:FragmentSettingBinding
    lateinit var mainActivity: MainActivity
    val profileInterface: ProfileInterface? = RetrofitClient.getClient()?.create(
        ProfileInterface::class.java)
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

        binding.ivBackArrow.setOnClickListener{
            mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_main, HomeFragment())
                .commit()
        }

        binding.layoutSettingAccount.setOnClickListener {
            mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_main, AccountFragment())
                .commit()

        }
        binding.layoutSettingAlarm.setOnClickListener {
            mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_main, AlarmFragment())
                .commit()
        }
        binding.layoutSettingFeedback.setOnClickListener {
            mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_main, FeedbackFragment())
                .commit()
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
                logout()
            }
            dialogView.btnNo.setOnClickListener{
                dialog.dismiss()
            }

        }
        return binding.root
    }
    fun logout(){
        val call = profileInterface?.logout()
        call?.enqueue(object: Callback<JsonElement> {
            override fun onResponse(
                call: Call<JsonElement>,
                response: Response<JsonElement>
            ){
                prefs.putSharedPreference(SHARED_PREFERENCE_NAME_JWT,"")
                Toast.makeText(mainActivity,"로그아웃 되었습니다",Toast.LENGTH_SHORT).show()
                val intent = Intent(mainActivity,SplashActivity::class.java)
                startActivity(intent)
                mainActivity.finish()
            }
            override fun onFailure(call: Call<JsonElement>, t: Throwable){
                Toast.makeText(mainActivity,"로그아웃에 실패했습니다${t} 잠시후 다시 시도해주세요", Toast.LENGTH_SHORT).show()
            }
        })
    }
}