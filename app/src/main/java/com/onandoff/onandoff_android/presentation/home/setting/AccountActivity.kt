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
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.user.UserInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.ProfileResult
import com.onandoff.onandoff_android.databinding.ActivityAccountBinding
import com.onandoff.onandoff_android.databinding.DialogPasswordBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.home.SettingActivity
import com.onandoff.onandoff_android.presentation.splash.SplashActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.SharePreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding

    private val userInterface: UserInterface? = RetrofitClient.getClient()?.create(
        UserInterface::class.java
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_account)
        binding.email = intent.getStringExtra("email")

        binding.ivBackArrow.setOnClickListener {
            finish()
        }
        binding.layoutAccountExit.setOnClickListener {
            deleteAccount()
        }
        binding.layoutPasswordReset.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this@AccountActivity)
        val dialogView = DialogPasswordBinding.inflate(LayoutInflater.from(this@AccountActivity))
        dialog.setContentView(dialogView.root)
        val params: WindowManager.LayoutParams? = dialog.window?.attributes;
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        if (params != null) {
            dialog.window?.setLayout(params.width, params.height)
        }
        dialog.show()
        dialogView.btnYes.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun deleteAccount() {
        val call = userInterface?.exitAccount()
        call?.enqueue(object : Callback<ProfileResult> {
            override fun onResponse(
                call: Call<ProfileResult>,
                response: Response<ProfileResult>,
            ) {
                SharePreference.prefs.putSharedPreference(
                    APIPreferences.SHARED_PREFERENCE_NAME_JWT,
                    ""
                )
                Toast.makeText(this@AccountActivity, "회원 탈퇴되었습니다", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@AccountActivity, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onFailure(call: Call<ProfileResult>, t: Throwable) {
                Toast.makeText(this@AccountActivity, "회원 탈퇴에 실패했습니다${t} 잠시후 다시 시도해주세요", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    companion object {
        fun getIntent(context: Context, email: String) =
            Intent(context, AccountActivity::class.java)
                .putExtra("email", email)
    }
}
