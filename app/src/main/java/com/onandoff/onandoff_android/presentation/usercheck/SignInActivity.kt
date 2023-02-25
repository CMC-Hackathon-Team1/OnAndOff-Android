package com.onandoff.onandoff_android.presentation.usercheck

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.onandoff.onandoff_android.data.api.user.ProfileInterface
import com.onandoff.onandoff_android.data.api.user.UserInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.ProfileListResponse
import com.onandoff.onandoff_android.data.model.ProfileListResultResponse
import com.onandoff.onandoff_android.data.model.SignInResponse
import com.onandoff.onandoff_android.data.model.SignRequest
import com.onandoff.onandoff_android.databinding.ActivitySigninBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.profile.ProfileCreateActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity:AppCompatActivity() {
    private lateinit var binding : ActivitySigninBinding
    var isExistBlank = false
    val userInterface: UserInterface? = RetrofitClient.getClient()?.create(UserInterface::class.java)
    val profileInterface: ProfileInterface? = RetrofitClient.getClient()?.create(ProfileInterface::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivArrow.setOnClickListener{
            finish()
        }
        binding.btSignin.setOnClickListener {
            val email = binding.etSigninEmail.text.toString()
            val password = binding.etSigninPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                isExistBlank = true
            }

            if(!isExistBlank){
                val user = SignRequest(email,password)
                val call = userInterface?.signIn(user)
                call?.enqueue(object : Callback<SignInResponse> {
                    override fun onResponse(
                        call: Call<SignInResponse>,
                        response: Response<SignInResponse>
                    ) {
                        val signInResponse = response.body()

                        when(signInResponse?.statusCode){
                            1101 -> {
                                Log.d(
                                    "Post",
                                    "retrofit manager called, onSucess called but not signIn"
                                );
                                Toast.makeText(this@SignInActivity,"회원 정보를 찾을수없습니다. 다시 확인해주세요",Toast.LENGTH_LONG).show()
                            }
                            1102->{
                                Toast.makeText(this@SignInActivity,"${signInResponse.message}",Toast.LENGTH_LONG).show()}

                            else -> {
                                Log.d(
                                    "Post",
                                    "retrofit manager called, onSucess called with ${signInResponse?.result?.jwt}"
                                );
                                signInResponse?.result?.let { it1 ->
                                    prefs.putSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_JWT,
                                        it1.jwt)
                                };
                                val call2 = profileInterface?.profileCheck()
                                call2?.enqueue(object : Callback<ProfileListResponse> {
                                    override fun onResponse(
                                        call: Call<ProfileListResponse>,
                                        response: Response<ProfileListResponse>
                                    ) {
                                        val profileResponse = response.body()
                                        when(profileResponse?.statusCode){
                                            1503 -> {
                                                Log.d(
                                                    "Get",
                                                    "retrofit manager called, onSucess called but profile not exits"
                                                );
                                                val profileIntent = Intent(this@SignInActivity, ProfileCreateActivity::class.java)
                                                startActivity(profileIntent)

                                            }
                                            else -> {
                                                Log.d(
                                                    "Get",
                                                    "retrofit manager called, onSucess called with ${response.body()}"
                                                );
                                                val list = mutableSetOf<String>()
                                                for(i in profileResponse?.result!!){
                                                    list.add(i.profileId.toString())
                                                }
                                                prefs.putSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID,list)

                                                prefs.putSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_EMAIL,email)
                                                val mainIntent = Intent(this@SignInActivity, MainActivity::class.java)
                                                startActivity(mainIntent)
                                            }
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<ProfileListResponse>,
                                        t: Throwable
                                    ) {
                                        Log.d(
                                            "Get",
                                            "retrofit manager called, onFailure called with ${t}"
                                        );
                                    }


                            })
                            }
                        }
                    }
                    override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                        Log.d(
                            "Post",
                            "retrofit manager called, onSucess called but already join!"
                        );
                        dialog("server error")
                    }
        })
    }else{
                dialog("blank")
            }
}
        binding.tvSignupLink.setOnClickListener{
            val Intent = Intent(this@SignInActivity, SignupActivity::class.java)
            startActivity(Intent)
        }
    }


    fun dialog( type:String){
        val dialog = AlertDialog.Builder(this);
        if (type.equals("success")){
            dialog.setTitle("로그인 성공!")
            dialog.setMessage("메인 화면으로 이동합니다")
        }else if (type.equals("blank")){
            dialog.setTitle("로그인 실패")
            dialog.setMessage("입력란을 모두 입력해주세요")
        }else if (type.equals("pw error")){
            dialog.setTitle("로그인 실패")
            dialog.setMessage("비밀번호가 다릅니다")
        }else if(type.equals("server error")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("서버 통신에 실패했습니다")
        }
        val dialogListener = object: DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                when(p1){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d("Dialog","다이얼로그 닫기!")
                }
            }
        }
        dialog.setPositiveButton("확인",dialogListener)
        dialog.show()
    }
}