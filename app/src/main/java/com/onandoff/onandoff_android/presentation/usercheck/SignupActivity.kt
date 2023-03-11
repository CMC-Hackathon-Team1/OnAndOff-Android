package com.onandoff.onandoff_android.presentation.usercheck

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.user.UserInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.SignRequest
import com.onandoff.onandoff_android.data.model.SignUpResponse
import com.onandoff.onandoff_android.databinding.ActivitySignupBinding
import com.onandoff.onandoff_android.presentation.splash.SignupEmailActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.SharePreference
import retrofit2.*
import java.util.regex.Pattern

class SignupActivity:AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    //log 태그
    val TAG : String = "회원가입"
    var isExistBlank = false
    var isPWSame = false
    val userInterface: UserInterface? = RetrofitClient.getClient()?.create(UserInterface::class.java)
    var isPwHide:Boolean = true
    var isPwRepeatHide:Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivBackArrow.setOnClickListener{
            finish()
        }
        binding.etSignupEmail.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.etSignupEmail.text.toString() !=""&&binding.etSignupPassword.text.toString() !=""&&binding.etSignupPasswordRepeat.text.toString() !=""){
                    binding.btSingup.setBackgroundResource(R.drawable.button_primary)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }

        )
        binding.etSignupPassword.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.etSignupEmail.text.toString() !=""&&binding.etSignupPassword.text.toString() !=""&&binding.etSignupPasswordRepeat.text.toString() !=""){
                    binding.btSingup.setBackgroundResource(R.drawable.button_primary)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }

        )
        binding.etSignupPasswordRepeat.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.etSignupEmail.text.toString() !=""&&binding.etSignupPassword.text.toString() !=""&&binding.etSignupPasswordRepeat.text.toString() !=""){
                    binding.btSingup.setBackgroundResource(R.drawable.button_primary)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }

        )
        binding.btnPasswordHide.setOnClickListener{
            if(isPwHide){
                Log.d("signup","$isPwHide")
                isPwHide = false
                binding.btnPasswordHide.setImageResource(R.drawable.ic_eye_open_16)
                binding.etSignupPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                Log.d("signup","${binding.etSignupPassword.inputType}")

            }else{
                isPwHide = true
                binding.btnPasswordHide.setImageResource(R.drawable.ic_eye_close_16)
                binding.etSignupPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD


            }
            binding.etSignupPassword.setSelection(binding.etSignupPassword.text.length);


        }
        binding.btnPasswordRepeatHide.setOnClickListener{
            if(isPwRepeatHide){
                Log.d("signup","$isPwRepeatHide")
                isPwRepeatHide = false
                binding.btnPasswordRepeatHide.setImageResource(R.drawable.ic_eye_open_16)
                binding.etSignupPasswordRepeat.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }else{
                isPwRepeatHide = true
                binding.btnPasswordRepeatHide.setImageResource(R.drawable.ic_eye_close_16)
                binding.etSignupPasswordRepeat.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.etSignupPasswordRepeat.setSelection(binding.etSignupPassword.text.length);

        }
        binding.btnEmailReset.setOnClickListener{
            binding.etSignupEmail.setText("")
        }
        binding.btnPasswordReset.setOnClickListener{
            binding.etSignupPassword.setText("")
        }
        binding.btnPasswordRepeatReset.setOnClickListener{
            binding.etSignupPasswordRepeat.setText("")
        }
        binding.btSingup.setOnClickListener{
            //1. email 입력값
            val email = binding.etSignupEmail.text.toString()
            //2. password 입력값
            val password = binding.etSignupPassword.text.toString()
            //3. password 확인 입력값
            val password_re = binding.etSignupPasswordRepeat.text.toString()


            //조건1) 두 필드중 입력을 안했다면
            if (email.isEmpty()) {
                isExistBlank = true
                setTextViewState(binding.tvSigninEmailError, binding.tvSigninEmailErrorLine, "이미 사용 중인 이메일 입니다.", true)
            } else {
                setTextViewState(binding.tvSigninEmailError, binding.tvSigninEmailErrorLine, "인증번호를 받기위해 정확한 이메일 주소를 입력해주세요.", false)
            }

            if (password.isEmpty()) {
                isExistBlank = true
                setTextViewState(binding.tvSigninPasswordError, binding.tvSigninPasswordErrorLine, "잘못된 입력입니다.(영문,특수문자 포함 8글자 이상)", true)
            } else {
                setTextViewState(binding.tvSigninPasswordError, binding.tvSigninPasswordErrorLine, "영문, 숫자, 특수문자 포함 8글자 이상", false)
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                setTextViewState(binding.tvSigninEmailError, binding.tvSigninEmailErrorLine, "잘못된 이메일입니다.", true)
                return@setOnClickListener
            }else{
                setTextViewState(binding.tvSigninEmailError, binding.tvSigninEmailErrorLine, "인증번호를 받기위해 정확한 이메일 주소를 입력해주세요.", false)
            }

            if (!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", password)) {
                setTextViewState(binding.tvSigninPasswordError, binding.tvSigninPasswordErrorLine, "잘못된 입력입니다.(영문,특수문자 포함 8글자 이상)", true)
                return@setOnClickListener
            }else{
                setTextViewState(binding.tvSigninPasswordError, binding.tvSigninPasswordErrorLine, "영문, 숫자, 특수문자 포함 8글자 이상", false)

            }

            if (password_re == password) {
                isPWSame = true
//                setTextViewState(binding.tvSigninPasswordError, binding.tvSigninPasswordErrorLine, "영문, 숫자, 특수문자 포함 8글자 이상", false)
                setTextViewState(binding.tvSigninPasswordSameError, binding.tvSigninPasswordSameErrorLine, "영문, 숫자, 특수문자 포함 8글자 이상", false)
            }else{
                setTextViewState(binding.tvSigninPasswordSameError, binding.tvSigninPasswordSameErrorLine, "위의 비밀번호와 다릅니다.", true)
            }

            if (!isExistBlank && isPWSame) {
                val user = SignRequest(email,password)
                val call = userInterface?.signUp(user,0)
                call?.enqueue(object :Callback<SignUpResponse> {
                    override fun onResponse(
                        call: Call<SignUpResponse>,
                        response: Response<SignUpResponse>
                    ) {
                        val body = response.body()
                        when (body?.statusCode) {
                        1100 -> {
                            Log.d(
                            "Post",
                            "retrofit manager called, onSucess called but already join!"
                            );
                            setTextViewState(binding.tvSigninEmailError, binding.tvSigninEmailErrorLine, "이미 사용중인 이메일입니다.", true)

                        }
                        else -> {
                            Log.d(
                                "Post",
                                "retrofit manager called, onSucess called with ${body}"
                            );
                            Toast.makeText(this@SignupActivity,"회원가입 성공! 이메일 인증단계로 넘어갑니다",Toast.LENGTH_SHORT).show()

                            SharePreference.prefs.putSharedPreference(
                                APIPreferences.SHARED_PREFERENCE_LIKE_NOTIFICATION_SETTING,
                                true)
                            SharePreference.prefs.putSharedPreference(
                                APIPreferences.SHARED_PREFERENCE_FOLLOW_NOTIFICATION_SETTING,
                                true)
                            SharePreference.prefs.putSharedPreference(
                                APIPreferences.SHARED_PREFERENCE_NOTICE_NOTIFICATION_SETTING,
                                true)

                            val Intent = Intent(this@SignupActivity, SignupEmailActivity::class.java)
                            Intent.putExtra("email",email)
                            Intent.putExtra("password",password)
                            startActivity(Intent)
                            finish()
                        }
                        }
                        }
                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                        Log.d(
                            "Post",
                            "retrofit manager called, onSucess called but already join!"
                        );
                        Toast.makeText(this@SignupActivity,"서버문제로 회원가입에 실패하였습니다.$t",Toast.LENGTH_SHORT).show()
                    }
                })
                }
            }
        }
    private fun setTextViewState(tv: TextView, view: View, errorMsg: String, isError: Boolean) {
        if (isError) {
            tv.text = errorMsg
            tv.setTextColor(getColor(R.color.errorColor))
            view.setBackgroundColor(getColor(R.color.errorColor))
        } else {
            tv.text = errorMsg
            tv.setTextColor(getColor(R.color.black_fifth))
            view.setBackgroundColor(getColor(R.color.black))
        }
    }






}