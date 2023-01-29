package com.onandoff.onandoff_android.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.util.Utility
import com.onandoff.onandoff_android.databinding.ActivitySplashBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.usercheck.SignInActivity
import com.onandoff.onandoff_android.presentation.usercheck.SignupActivity
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs


class SplashActivity:AppCompatActivity() {
    private lateinit var binding:ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        var keyHash = Utility.getKeyHash(this)
//        Log.d("Hash", keyHash)
        var userEmail = prefs.getSharedPreference("email", "")
        Log.d("splash", userEmail)

        if(userEmail != ""){
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "로그인 되었습니다", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.btServiceLogin.setOnClickListener{
            val Intent = Intent(this, SignInActivity::class.java)
            startActivity(Intent)
        }

        binding.btLoginKakao.setOnClickListener({
    //new View.OnClickListener() {
//            @Override
//            public void onClick(View view){
//                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(login.this)) {
//                    UserApiClient.getInstance().loginWithKakaoTalk(login.this, callback);
//                }else {
//                UserApiClient.getInstance().loginWithKakaoAccount(login.this, callback);
//            }
//            }

        });
//        binding.btLoginNaver.setOnClickListener{}
//        binding.btLoginGoogle.setOnClickListener{}

    }

}