package com.onandoff.onandoff_android.presentation.usercheck

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.onandoff.onandoff_android.data.api.user.UserInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.SignRequest
import com.onandoff.onandoff_android.data.model.SignResponse
import com.onandoff.onandoff_android.databinding.ActivitySignupBinding
import retrofit2.*

class SignupActivity:AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    //log 태그
    val TAG : String = "회원가입"
    var isExistBlank = false
    var isPWSame = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btSingup.setOnClickListener {
            val email = binding.etSignupEmail.text.toString()
            val password = binding.etSignupPassword.text.toString()
            val password_re = binding.etSignupPasswordRepeat.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                isExistBlank = true
            } else {
                if (password_re == password) {
                    isPWSame = true
                }
            }
            if (!isExistBlank && isPWSame) {
                val userInterface: UserInterface? = RetrofitClient.getClient()?.create(UserInterface::class.java)
                val user = SignRequest(email,password)
                val call = userInterface?.signUp(user)
                call?.enqueue(object :Callback<SignResponse> {
                    override fun onResponse(
                        call: Call<SignResponse>,
                        response: Response<SignResponse>
                    ) {
                        val body = response.body()
                        when (body?.statusCode) {
                        1100 -> {
                            Log.d(
                            "Post",
                            "retrofit manager called, onSucess called but already join!"
                            );
                           dialog("user exist")
                        }
                        else -> {
                            Log.d(
                                "Post",
                                "retrofit manager called, onSucess called with ${body}"
                            );
                            Toast.makeText(this@SignupActivity,"회원가입 성공! 로그인해주세요:)",Toast.LENGTH_SHORT).show()
                            val Intent = Intent(this@SignupActivity, SignInActivity::class.java)
                            startActivity(Intent)
                            finish()
                        }
                        }
                        }
                    override fun onFailure(call: Call<SignResponse>, t: Throwable) {
                        Log.d(
                            "Post",
                            "retrofit manager called, onSucess called but already join!"
                        );
                        dialog("server error")
                    }
                })
                } else {
                if (isExistBlank) {
                    dialog("blank")
                } else if (!isPWSame) {
                    dialog("pw error")
                }
            }
        }
    }
    fun dialog( type:String){
        val dialog = AlertDialog.Builder(this);
        if (type.equals("success")){
            dialog.setTitle("회원가입 성공!")
            dialog.setMessage("프로필 생성 페이지로 이동합니다")
        }else if (type.equals("user exist")){
            dialog.setTitle("회원가입  실패")
            dialog.setMessage("이미 가입된 계정이 있습니다")
        }else if (type.equals("blank")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 입력해주세요")
        }else if (type.equals("pw error")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("비밀번호가 다릅니다")
        }else if(type.equals("server error")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("서버 통신에 실패했습니다")
        }
        val dialogListener = object: DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                when(p1){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG,"다이얼로그 닫기!")
                }
            }
        }
        dialog.setPositiveButton("확인",dialogListener)
        dialog.show()
    }





}