package com.onandoff.onandoff_android.presentation.home

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonElement
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.user.UserInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.getMyEmail
import com.onandoff.onandoff_android.databinding.ActivitySettingBinding
import com.onandoff.onandoff_android.databinding.DialogLogoutBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.home.setting.*
import com.onandoff.onandoff_android.presentation.splash.SplashActivity
import com.onandoff.onandoff_android.util.APIPreferences.SHARED_PREFERENCE_NAME_JWT
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    private val userInterface: UserInterface? = RetrofitClient.getClient()?.create(
        UserInterface::class.java
    )
    var email: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setOnClickListeners()
        getEmail()
    }

    private fun setOnClickListeners() {
        binding.ivBackArrow.setOnClickListener {
            finish()
        }

        binding.layoutSettingAccount.setOnClickListener {
            val intent = AccountActivity.getIntent(this@SettingActivity, email)
            startActivity(intent)

        }
        binding.layoutSettingBlockedUserList.setOnClickListener {
            val intent = BlockedUserListActivity.getIntent(this)
            startActivity(intent)
        }
        binding.layoutSettingAlarm.setOnClickListener {
            val intent = AlarmActivity.getIntent(this)
            startActivity(intent)
        }
        binding.layoutSettingFeedback.setOnClickListener {
            val intent = FeedbackActivity.getIntent(this)
            startActivity(intent)
        }
        binding.layoutSettingPolicy.setOnClickListener {
            val intent = PolicyActivity.getIntent(this)
            startActivity(intent)
        }
        binding.layoutSettingLogout.setOnClickListener {
            val dialog = Dialog(this@SettingActivity)
            val dialogView = DialogLogoutBinding.inflate(LayoutInflater.from(this@SettingActivity))
            dialog.setContentView(dialogView.root)
            val params: WindowManager.LayoutParams? = dialog.window?.attributes;
            params?.width = WindowManager.LayoutParams.MATCH_PARENT
            params?.height = WindowManager.LayoutParams.WRAP_CONTENT
            if (params != null) {
                dialog.window?.setLayout(params.width, params.height)
            }
            dialog.show()
            dialogView.btnYes.setOnClickListener {
                logout()
            }
            dialogView.btnNo.setOnClickListener {
                dialog.dismiss()
            }

        }
    }

    private fun logout() {
        val call = userInterface?.logout()
        call?.enqueue(object : Callback<JsonElement> {
            override fun onResponse(
                call: Call<JsonElement>,
                response: Response<JsonElement>,
            ) {
                prefs.putSharedPreference(SHARED_PREFERENCE_NAME_JWT, "")
                Toast.makeText(this@SettingActivity, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SettingActivity, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Toast.makeText(
                    this@SettingActivity,
                    "로그아웃에 실패했습니다${t} 잠시후 다시 시도해주세요",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getEmail() {
        val call = userInterface?.getEmail()
        call?.enqueue(object : Callback<getMyEmail> {
            override fun onResponse(
                call: Call<getMyEmail>,
                response: Response<getMyEmail>,
            ) {
                email = response.body()?.result?.email.orEmpty()
                Log.d("setting", email)
            }

            override fun onFailure(call: Call<getMyEmail>, t: Throwable) {

            }
        })
    }

    companion object {
        fun getIntent(context: Context) =
            Intent(context, SettingActivity::class.java)
    }
}