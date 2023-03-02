package com.onandoff.onandoff_android.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.ActivitySignupPolicyBinding
import com.onandoff.onandoff_android.presentation.usercheck.SignInActivity

class SignupPolicyActivity:AppCompatActivity() {
    private lateinit var binding:ActivitySignupPolicyBinding
    var policyAllCheck: Boolean = false
    var policyAge: Boolean = false
    var policyService: Boolean = false
    var policyPrivacy: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignupPolicyBinding.inflate(layoutInflater)
       
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        binding.apply {
            btnAllcheck.setOnClickListener {
                if (policyAllCheck) {
                    btnAllcheck.setImageResource(R.drawable.button_circle)
                    btnAgecheck.setImageResource(R.drawable.button_circle)
                    btnPrivatecheck.setImageResource(R.drawable.button_circle)
                    btnServicecheck.setImageResource(R.drawable.button_circle)
                    policyAllCheck = false
                    policyAge = false
                    policyService = false
                    policyPrivacy = false
                    binding.btnAgreeService.setBackgroundResource(R.drawable.button_disable)

                } else {
                    btnAllcheck.setImageResource(R.drawable.ic_check_15)
                    btnAgecheck.setImageResource(R.drawable.ic_check_15)
                    btnPrivatecheck.setImageResource(R.drawable.ic_check_15)
                    btnServicecheck.setImageResource(R.drawable.ic_check_15)
                    policyAllCheck = true
                    policyAge = true
                    policyService = true
                    policyPrivacy = true
                    binding.btnAgreeService.setBackgroundResource(R.drawable.button_primary)
                }

            }
            //individual check
            btnAgecheck.setOnClickListener {
                if (policyAge) {
                    it.setBackgroundResource(R.drawable.button_circle)
                    policyAge = false;
                } else {
                    it.setBackgroundResource(R.drawable.ic_check_15)
                    policyAge = true
                }
                isAllCheck()
            }
            btnServicecheck.setOnClickListener {
                if (policyAge) {
                    it.setBackgroundResource(R.drawable.button_circle)
                    policyService = false;
                } else {
                    it.setBackgroundResource(R.drawable.ic_check_15)
                    policyService = true
                }
                isAllCheck()

            }
            btnPrivatecheck.setOnClickListener {
                if (policyPrivacy) {
                    it.setBackgroundResource(R.drawable.button_circle)
                    policyPrivacy = false;
                } else {
                    it.setBackgroundResource(R.drawable.ic_check_15)
                    policyPrivacy = true
                }
                isAllCheck()
            }
           btnAgreeService.setOnClickListener {
               if (policyAge &&policyService&&policyPrivacy or policyAllCheck ){
                   it.setBackgroundResource(R.drawable.button_primary)
                   val Intent = Intent(this@SignupPolicyActivity, SignInActivity::class.java)
                   startActivity(Intent)
               }else {
                   Toast.makeText(this@SignupPolicyActivity,"모든 필수 권한에 동의해야 서비스를 이용할수있습니다.",Toast.LENGTH_LONG).show()
               }
           }
        }
    }
    fun isAllCheck(){
        if (policyAllCheck or policyAge && policyService && policyPrivacy){
                binding.btnAgreeService.setBackgroundResource(R.drawable.button_primary)
            }else{
                binding.btnAgreeService.setBackgroundResource(R.drawable.button_disable)
        }
    }
}