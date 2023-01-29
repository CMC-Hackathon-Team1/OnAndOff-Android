package com.onandoff.onandoff_android.presentation.usercheck

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.onandoff.onandoff_android.data.api.user.UserInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.SignRequest
import com.onandoff.onandoff_android.data.model.SignResponse
import com.onandoff.onandoff_android.databinding.ActivitySigninBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity:AppCompatActivity() {
    private lateinit var binding : ActivitySigninBinding
    var isExistBlank = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btSignin.setOnClickListener {
            val email = binding.etSigninEmail.text.toString()
            val password = binding.etSigninPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                isExistBlank = true
            }
            if(!isExistBlank){
                val userInterface: UserInterface? = RetrofitClient.getClient()?.create(UserInterface::class.java)
                val user = SignRequest(email,password)
                val call = userInterface?.signIn(user)
                call?.enqueue(object : Callback<SignResponse> {
                    override fun onResponse(
                        call: Call<SignResponse>,
                        response: Response<SignResponse>
                    ) {
                        val header = response.headers()
                        val cookie = header.get("Set-Cookie")?.split("=")?.get(1)?.split(";")?.get(0)




                    }

                    override fun onFailure(call: Call<SignResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
        })
    }
}
    }}