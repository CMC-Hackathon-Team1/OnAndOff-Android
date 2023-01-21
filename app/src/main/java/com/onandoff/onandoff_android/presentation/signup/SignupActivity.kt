package com.onandoff.onandoff_android.presentation.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onandoff.onandoff_android.databinding.ActivitySignupBinding

class SignupActivity:AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.btSingup.setOnClickListener();
    }
}