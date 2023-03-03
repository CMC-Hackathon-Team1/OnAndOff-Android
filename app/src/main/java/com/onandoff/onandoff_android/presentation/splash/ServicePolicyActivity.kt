package com.onandoff.onandoff_android.presentation.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onandoff.onandoff_android.databinding.ActivityServicePolicyBinding

class ServicePolicyActivity:AppCompatActivity() {
    private lateinit var binding:ActivityServicePolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServicePolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivBackArrow.setOnClickListener{
            finish()
        }
    }
}