package com.onandoff.onandoff_android.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Constraints
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.onandoff.onandoff_android.data.api.user.UserInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.KakaoRequest
import com.onandoff.onandoff_android.data.model.KakaoResponse
import com.onandoff.onandoff_android.databinding.ActivitySplashBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.profile.ProfileCreateActivity
import com.onandoff.onandoff_android.presentation.usercheck.SignInActivity
import com.onandoff.onandoff_android.util.APIPreferences.SHARED_PREFERENCE_NAME_JWT
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashActivity:AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    // 카카오계정으로 로그인 공통 callback 구성
    // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(Constraints.TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(Constraints.TAG, "카카오계정으로 로그인 성공")
            kakaoApi(token)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)
        var userEmail = prefs.getSharedPreference("email", "")
        Log.d("splash", userEmail)

//        if (userEmail != "") {
//            val intent = Intent(applicationContext, MainActivity::class.java)
//            startActivity(intent)
//            Toast.makeText(this, "로그인 되었습니다", Toast.LENGTH_SHORT).show()
//            finish()
//        }
        binding.btServiceLogin.setOnClickListener {
            val Intent = Intent(this, SignInActivity::class.java)
            startActivity(Intent)
        }

        binding.btKakaoLogin.setOnClickListener {

            Log.d("fd", "fdsf");
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@SplashActivity)) {
                UserApiClient.instance.loginWithKakaoTalk(this@SplashActivity) { token, error ->
                    if (error != null) {
                        Log.e(Constraints.TAG, "로그인 실패", error)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            Log.e(Constraints.TAG, "로그인 취소", error)
                        }
                    } else if (token != null) {
                        Log.i(Constraints.TAG, "카카오앱으로 로그인 성공")
                        kakaoApi(token)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(
                    this@SplashActivity,
                    callback = callback
                )
            }


        }
    }

    //카카오에서 반환해준 토큰을 우리쪽 서버와 통신하여 jwt를 얻는 함수
    fun kakaoApi(token: OAuthToken) {
        Log.i(Constraints.TAG, "토큰반환 성공 ${token.accessToken}")
        val userInterface: UserInterface? =
            RetrofitClient.getClient()?.create(UserInterface::class.java)
        val call = userInterface?.kakaoLogIn(KakaoRequest(token.accessToken))
        call?.enqueue(object : Callback<KakaoResponse> {
            override fun onResponse(
                call: Call<KakaoResponse>,
                response: Response<KakaoResponse>
            ) {
                val body = response.body()
                response.body()?.result?.jwt?.let { Log.d("성공!", it) };
                prefs.putSharedPreference(
                    SHARED_PREFERENCE_NAME_JWT,
                    body?.result?.jwt!!
                )
                Log.d("Splash",body?.result.state)
                if(body?.result.state == "로그인 완료"){
                    Log.d("Splash","check")
                    val intent  = Intent(this@SplashActivity,ProfileCreateActivity::class.java)
//                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)

                }else{
                    Log.d("Splash","check22")
                    val intent  = Intent(this@SplashActivity,ProfileCreateActivity::class.java)
                    startActivity(intent)

//                    val Intent = Intent(this@SplashActivity, SignupPolicyActivity::class.java)
                }
//                Log.d("Splash","check22")
//                finish()
            }

            override fun onFailure(call: Call<KakaoResponse>, t: Throwable) {
                Log.d("실패!", t.toString());
            }

        })
    }
}