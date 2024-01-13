package com.onandoff.onandoff_android.presentation.home.setting

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.user.UserInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.ProfileResult
import com.onandoff.onandoff_android.databinding.ActivityFeedbackBinding
import com.onandoff.onandoff_android.databinding.DialogFeedbackCompleteBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.home.HomeFragment
import com.onandoff.onandoff_android.presentation.home.SettingActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedbackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedbackBinding
    private val userInterface: UserInterface? = RetrofitClient.getClient()?.create(
        UserInterface::class.java
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivBackArrow.setOnClickListener {
            finish()
        }
        binding.tvFeedbackSubmit.setOnClickListener {
            sendFeedback(binding.etFeedbackText.text.toString())
//            finish()
        }
        binding.etFeedbackText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.etFeedbackText.text.toString() != "") {
                    binding.tvFeedbackSubmit.setBackgroundResource(R.drawable.button_primary)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }


    private fun sendFeedback(feedText: String) {
        val call = userInterface?.sendFeedBack(feedText)
        call?.enqueue(object : Callback<ProfileResult> {
            override fun onResponse(
                call: Call<ProfileResult>,
                response: Response<ProfileResult>,
            ) {
                showDialog()
                finish()
            }

            override fun onFailure(call: Call<ProfileResult>, t: Throwable) {
                Toast.makeText(
                    this@FeedbackActivity,
                    "통신에 실패했습니다${t} 잠시후 다시 시도해주세요",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun showDialog() {
        val dialog = Dialog(this@FeedbackActivity)
        val dialogView =
            DialogFeedbackCompleteBinding.inflate(LayoutInflater.from(this@FeedbackActivity))
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

    companion object {
        fun getIntent(context: Context) =
            Intent(context, FeedbackActivity::class.java)
    }
}
