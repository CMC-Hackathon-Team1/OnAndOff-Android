package com.onandoff.onandoff_android.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.onandoff.onandoff_android.data.api.user.UserInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.SignRequest
import com.onandoff.onandoff_android.data.model.SignUpResponse
import com.onandoff.onandoff_android.databinding.ActivitySignupEmailBinding
import com.onandoff.onandoff_android.databinding.ActivitySignupPolicyBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupEmailActivity:AppCompatActivity() {
    private lateinit var binding: ActivitySignupEmailBinding
    val userInterface: UserInterface? = RetrofitClient.getClient()?.create(UserInterface::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val intent = getIntent() //전달할 데이터를 받을 Intent
        val email = intent.getStringExtra("email")
        binding.email = email
        val password = intent.getStringExtra("password")
        Log.d("email verify","$email $password")
        binding.btnEmailVerify.setOnClickListener {

            if (email != null && password != null) {
                val user = SignRequest(email, password)
                val call = userInterface?.signUp(user, 1)
                call?.enqueue(object : Callback<SignUpResponse> {
                    override fun onResponse(
                        call: Call<SignUpResponse>,
                        response: Response<SignUpResponse>
                    ) {
                        val body = response.body()
                        when (body?.statusCode) {
                            1006 -> {
                                Log.d(
                                    "Post",
                                    "email form eror"
                                );
                            }
                            else -> {
                                Log.d(
                                    "Post",
                                    "retrofit manager called, onSucess called with ${body}"
                                );

                                val Intent =
                                    Intent(this@SignupEmailActivity, SignupPolicyActivity::class.java)
                                startActivity(Intent)
                                finish()
                            }
                        }
                    }

                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                        Log.d(
                            "Post",
                            "retrofit manager called, onSucess called but$t"
                        );
                    }
                })
            } else {
                Toast.makeText(
                    this@SignupEmailActivity,
                    "email혹은 password를 다시 입력해주세요!",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }

    }
}
