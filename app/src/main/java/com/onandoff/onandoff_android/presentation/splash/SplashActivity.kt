package com.onandoff.onandoff_android.presentation.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onandoff.onandoff_android.databinding.ActivitySplashBinding

class SplashActivity:AppCompatActivity() {
    private lateinit var binding:ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.btLoginGoogle.setOnClickListener{}
//        binding.btLoginKakao.setOnClickListener{}
//        binding.btLoginNaver.setOnClickListener{}
//        binding.btServiceLogin.setOnClickListener{}
    }

}