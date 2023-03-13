package com.onandoff.onandoff_android.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.user.ProfileInterface
import com.onandoff.onandoff_android.data.api.user.UserInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.GoogleRequest
import com.onandoff.onandoff_android.data.model.KakaoRequest
import com.onandoff.onandoff_android.data.model.ProfileListResponse
import com.onandoff.onandoff_android.data.model.SocialLoginResponse
import com.onandoff.onandoff_android.databinding.ActivitySplashBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.profile.ProfileCreateActivity
import com.onandoff.onandoff_android.presentation.usercheck.SignInActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.APIPreferences.SHARED_PREFERENCE_NAME_JWT
import com.onandoff.onandoff_android.util.APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashActivity:AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    //startforactivity에서 코드
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

//        val account = GoogleSignIn.getLastSignedInAccount(this@SplashActivity)
//        account?.idToken?.let { googleApi(it) } ?: Toast.makeText(this,"google 로그인에 실패하였습니다. 다시시도해주세요",Toast.LENGTH_SHORT).show()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)
        var jwt = prefs.getSharedPreference(SHARED_PREFERENCE_NAME_JWT, "")
        if(jwt!=""){
            checkProfile()
//            val intent = Intent(this@SplashActivity, MainActivity::class.java)
//            startActivity(intent)
            Toast.makeText(this, "자동 로그인 되었습니다", Toast.LENGTH_SHORT).show()
//            finish()
        }
//        Log.d("splash", userEmail)


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

        //1. 앱에 필요한 사용자 데이터를 요청하도록 로그인 옵션을 설정해줌
        //DEFAULT_SIGN_IN parameter로 유저의 ID, 기본적 프로필 정보 요청
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()//email 요청
            .build()
        //2. 위에서 만든 gso를 파라미터로 넣어줘서 googlesignInClient 객체를 만들어줌
        val mGoogleSignInClient = GoogleSignIn.getClient(this@SplashActivity, gso);
        binding.btGoogleLogin.setOnClickListener{
            //3. signIn할수있는 intent를 생성
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //4. startActivityForResult로 인해 실행된 코드
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    //5. 사용자 정보 가져오기
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            Log.d("Google",completedTask.isSuccessful.toString())
            val account = completedTask.getResult(ApiException::class.java)

            Log.d("Google",account.account.toString())
            Log.d("Google",account.displayName.toString())
            Log.d("Google",account.idToken.toString())
            Log.d("Google",account.id.toString())
            account.idToken?.let { googleApi(it) } ?:
                Toast.makeText(this,"google 로그인에 실패하였습니다. 다시시도해주세요",Toast.LENGTH_SHORT).show()

            // Signed in successfully, show authenticated UI.
//            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("Google","google 로그인에 실패하였습니다. 다시시도해주세요${e}")

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


            body?.result?.let { Log.d("Splash", it.state) }
            if(body?.result?.state == "로그인 완료"){
                prefs.putSharedPreference(
                    SHARED_PREFERENCE_NAME_JWT,
                    body?.result?.jwt!!
                )
                checkProfile()
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
    fun googleApi(token:String){
        val call = userInterface?.googleLogIn(GoogleRequest(token))
        call?.enqueue(object :Callback<SocialLoginResponse>{
            override fun onResponse(
                call: Call<SocialLoginResponse>,
                response: Response<SocialLoginResponse>
            ) {

                if(response.body()?.result?.state == "회원가입 완료" ){
                    Log.d("Google","이거 된거 맞음?")
                    prefs.putSharedPreference(SHARED_PREFERENCE_NAME_JWT,
                        response.body()?.result?.jwt!!
                    )
                    val intent = Intent(this@SplashActivity, ProfileCreateActivity::class.java)
                    startActivity(intent)
                        finish()
                }else if(response.body()?.result?.state == "로그인 완료" ){
                    prefs.putSharedPreference(SHARED_PREFERENCE_NAME_JWT,
                        response.body()?.result?.jwt!!
                    )
                    checkProfile()
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Log.d("Google",response.body().toString())
                }


            }

            override fun onFailure(call: Call<SocialLoginResponse>, t: Throwable) {
                Log.d("Google","${t}")

            }

        })
    }

    fun checkProfile() {
        val call2 = profileInterface?.profileCheck()
        call2?.enqueue(object : Callback<ProfileListResponse> {
            override fun onResponse(
                call: Call<ProfileListResponse>,
                response: Response<ProfileListResponse>
            ) {
                val profileResponse = response.body()
                when (profileResponse?.statusCode) {
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
//                                val list = mutableSetOf<String>()
//                                for(i in profileResponse?.result!!){
//                                    list.add(i.profileId.toString())
//                                }
//                                prefs.putSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_PROFILEID,list)

                        val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(mainIntent)
                        finish()
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