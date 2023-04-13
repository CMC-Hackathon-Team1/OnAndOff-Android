package com.onandoff.onandoff_android.presentation.home.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.ActivityPolicyBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.home.SettingActivity
import com.onandoff.onandoff_android.presentation.splash.PrivatePolicyActivity
import com.onandoff.onandoff_android.presentation.splash.ServicePolicyActivity

class PolicyActivity:AppCompatActivity() {
    private lateinit var binding:ActivityPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPolicyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivBackArrow.setOnClickListener{
            finish()
        }
        binding.ivPrivatePolicyMore.setOnClickListener{
            val intent = Intent(this@PolicyActivity, PrivatePolicyActivity::class.java)
            startActivity(intent)
        }
        binding.ivServicePolicyMore.setOnClickListener{
            val intent = Intent(this@PolicyActivity, ServicePolicyActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        fun getIntent(context: Context) =
            Intent(context, PolicyActivity::class.java)
    }
}