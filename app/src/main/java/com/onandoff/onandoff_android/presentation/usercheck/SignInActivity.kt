package com.onandoff.onandoff_android.presentation.usercheck

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onandoff.onandoff_android.databinding.ActivitySigninBinding

class SignInActivity:AppCompatActivity() {
    private lateinit var binding : ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}