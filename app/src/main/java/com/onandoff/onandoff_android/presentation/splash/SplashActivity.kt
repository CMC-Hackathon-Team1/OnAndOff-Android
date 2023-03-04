package com.onandoff.onandoff_android.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Constraints
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.onandoff.onandoff_android.data.api.user.ProfileInterface
import com.onandoff.onandoff_android.data.api.user.UserInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.KakaoRequest
import com.onandoff.onandoff_android.data.model.ProfileListResponse
import com.onandoff.onandoff_android.data.model.SocialLoginResponse
import com.onandoff.onandoff_android.databinding.ActivitySplashBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.profile.ProfileCreateActivity
import com.onandoff.onandoff_android.presentation.usercheck.SignInActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.APIPreferences.SHARED_PREFERENCE_NAME_JWT
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashActivity:AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    val RC_SIGN_IN = 200
    val profileInterface: ProfileInterface? = RetrofitClient.getClient()?.create(ProfileInterface::class.java)
    val userInterface: UserInterface? =
        RetrofitClient.getClient()?.create(UserInterface::class.java)
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
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        val account = GoogleSignIn.getLastSignedInAccount(this)
//        updateUI(account)
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
        binding.btGoogleLogin.setOnClickListener{
            //TODO : Google Login code 작성
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
//            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
//            updateUI(null)
        }
    }
//카카오에서 반환해준 토큰을 우리쪽 서버와 통신하여 jwt를 얻는 함수
fun kakaoApi(token: OAuthToken) {
    Log.i(Constraints.TAG, "토큰반환 성공 ${token.accessToken}")

    val call = userInterface?.kakaoLogIn(KakaoRequest(token.accessToken))
    call?.enqueue(object : Callback<SocialLoginResponse> {
        override fun onResponse(
            call: Call<SocialLoginResponse>,
            response: Response<SocialLoginResponse>
        ) {
            val body = response.body()
            response.body()?.result?.jwt?.let { Log.d("성공!", it) };
            prefs.putSharedPreference(
                SHARED_PREFERENCE_NAME_JWT,
                body?.result?.jwt!!
            )

            Log.d("Splash",body?.result.state)
            if(body?.result.state == "로그인 완료"){
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
                                val intent = Intent(this@SplashActivity, ProfileCreateActivity::class.java)
                                startActivity(intent)
                                finish()

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

                                val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
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
            else{
                val intent = Intent(this@SplashActivity, ProfileCreateActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

        override fun onFailure(call: Call<SocialLoginResponse>, t: Throwable) {
            Log.d("실패!", t.toString());
        }

    })
}
}