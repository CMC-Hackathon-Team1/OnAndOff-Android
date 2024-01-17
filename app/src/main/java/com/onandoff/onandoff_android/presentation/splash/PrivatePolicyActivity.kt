package com.onandoff.onandoff_android.presentation.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onandoff.onandoff_android.databinding.ActivityPrivatePolicyBinding

class PrivatePolicyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivatePolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivatePolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBackArrow.setOnClickListener {
            finish()
        }
    }
}
