package com.onandoff.onandoff_android.presentation.home.setting

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.user.ProfileInterface
import com.onandoff.onandoff_android.data.api.user.UserInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.ProfileResult
import com.onandoff.onandoff_android.databinding.DialogPasswordBinding
import com.onandoff.onandoff_android.databinding.FragmentAccountBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.home.SettingFragment
import com.onandoff.onandoff_android.presentation.splash.SplashActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountFragment :Fragment(){
    private lateinit var binding:FragmentAccountBinding
    lateinit var mainActivity: MainActivity
    val userInterface: UserInterface? = RetrofitClient.getClient()?.create(
        UserInterface::class.java)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var email = arguments?.getString("email")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        binding.email = email
        binding.ivBackArrow.setOnClickListener{
            mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_main, SettingFragment())
                .commit()
        }
        binding.layoutAccountExit.setOnClickListener {
            deleteAccount()
        }
        binding.layoutPasswordReset.setOnClickListener {
            showDialog()
        }
        return binding.root
    }
    fun showDialog(){
        val dialog = Dialog(mainActivity)
        val dialogView = DialogPasswordBinding.inflate(LayoutInflater.from(mainActivity))
        dialog.setContentView(dialogView.root)
        val params : WindowManager.LayoutParams? = dialog.window?.attributes;
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        if (params != null) {
            dialog.window?.setLayout(params.width,params.height)
        }
        dialog.show()
        dialogView.btnYes.setOnClickListener{
            dialog.dismiss()
        }
    }
    fun deleteAccount(){
        val call = userInterface?.exitAccount()
        call?.enqueue(object: Callback<ProfileResult> {
            override fun onResponse(
                call: Call<ProfileResult>,
                response: Response<ProfileResult>
            ){
                SharePreference.prefs.putSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_JWT,"")
                Toast.makeText(mainActivity,"회원탈퇴 되었습니다", Toast.LENGTH_SHORT).show()
                val intent = Intent(mainActivity, SplashActivity::class.java)
                startActivity(intent)
                mainActivity.finish()
            }
            override fun onFailure(call: Call<ProfileResult>, t: Throwable){
                Toast.makeText(mainActivity,"회원탈퇴에 실패했습니다${t} 잠시후 다시 시도해주세요", Toast.LENGTH_SHORT).show()
            }
        })
    }
}