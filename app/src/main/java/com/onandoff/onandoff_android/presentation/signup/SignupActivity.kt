package com.onandoff.onandoff_android.presentation.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onandoff.onandoff_android.data.api.RetrofitManager
import com.onandoff.onandoff_android.data.model.User
import com.onandoff.onandoff_android.data.model.UserSignUp
import com.onandoff.onandoff_android.databinding.ActivitySignupBinding

class SignupActivity:AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btSingup.setOnClickListener{
            val email = binding.etSignupEmail.text.toString()
            val password = binding.etSignupPassword.text.toString()
            RetrofitManager.instance.signUp(userSignUp = UserSignUp(email,password), completion = {it})
        }
    }
}