package com.onandoff.onandoff_android.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.common.util.Utility
import com.onandoff.onandoff_android.databinding.ActivitySplashBinding
import com.onandoff.onandoff_android.presentation.usercheck.SignupActivity


class SplashActivity:AppCompatActivity() {
    private lateinit var binding:ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)
        binding.btServiceLogin.setOnClickListener{
            val Intent = Intent(this, SignupActivity::class.java)
//            Intent.putExtra("splash", "스플래시")
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