package com.onandoff.onandoff_android.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.messaging.FirebaseMessaging
import com.onandoff.onandoff_android.FragmentAdapter
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.notification.NotificationInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.NotificationResponse
import com.onandoff.onandoff_android.data.model.TokenData
import com.onandoff.onandoff_android.databinding.ActivityMainBinding
import com.onandoff.onandoff_android.presentation.home.HomeFragment
import com.onandoff.onandoff_android.presentation.look.LookAroundFragment
import com.onandoff.onandoff_android.presentation.mypage.MypageFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private var isBackPressed = false // 뒤로가기 버튼 두 번 눌렸는지 체크하는 변수

    override fun onBackPressed() {
        if (!isBackPressed) {
            Toast.makeText(this@MainActivity, "한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
            isBackPressed = true // 첫 번째 뒤로가기 버튼 클릭 시 true로 변경
        } else {
            super.onBackPressed() // 두 번째 뒤로가기 버튼 클릭 시 앱 종료
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        setupFragmentAdapter()
        setupBottomNavigationView()

        initFirebase()

        if (savedInstanceState == null) {
            binding.bottomNavMain.selectedItemId = R.id.menu_home
        }
    }

    private fun initFirebase() {
        val notificationInterface: NotificationInterface? = RetrofitClient.getClient()?.create(
            NotificationInterface::class.java)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "FCM 토큰 얻기에 실패하였습니다.", task.exception)
                return@OnCompleteListener
            }
            // token log 남기기
            Log.d(TAG, "token: ${task.result?:"task.result is null"}")
            if(task.result != null){
                uploadToken(task.result!!)

                val token = task.result
                val tokenData = TokenData(token)

                notificationInterface?.setAlarmTokenResponse(tokenData)?.enqueue(object :
                    Callback<NotificationResponse> {
                    override fun onResponse(
                        call: Call<NotificationResponse>,
                        response: Response<NotificationResponse>
                    ) {
                        if (response.code() == 201){
                            Log.d(TAG, "onResponse: Fcm token setting complete")
                        }
                    }

                    override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }
        })

        createNotificationChannel(channel_id, "notice")
    }


    private fun createNotificationChannel(id: String, name: String) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(id, name, importance)

        val notificationManager: NotificationManager
                = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    companion object{
        // Notification Channel ID
        const val channel_id = "notification_channel"
        fun uploadToken(token:String){
            // 새로운 토큰 수신 시 서버로 전송
            val data = TokenData(token)
            val storeService = RetrofitClient.getClient()?.create(NotificationInterface::class.java)
            storeService?.setAlarmTokenResponse(data)?.enqueue(object : Callback<NotificationResponse> {
                override fun onResponse(call: Call<NotificationResponse>, response: Response<NotificationResponse>) {
                    if(response.isSuccessful){
                        val res = response.body()
                        Log.d(TAG, "onResponse: $res")
                    } else {
                        Log.d(TAG, "onResponse: Error Code ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                    Log.d(TAG, t.message ?: "토큰 정보 등록 중 통신오류")
                }
            })
        }
    }

    private fun setupFragmentAdapter() {
        val fragmentAdapter = FragmentAdapter(this)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.commit()
//        fragmentTransaction.add(binding.mainFcv.id, recordFragment, binding.mainFcv.tag)

    }

    private fun setupBottomNavigationView() {
        binding.bottomNavMain.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val prevFragment = supportFragmentManager.fragments.find {
            it.isVisible
        }

        if (prevFragment != null) {
            supportFragmentManager.beginTransaction().hide(prevFragment).commitNow()
        }

        when (item.itemId) {
            R.id.menu_home -> {
                val homeFragment = supportFragmentManager.fragments.find { it is HomeFragment }
                if (homeFragment != null) {
                    supportFragmentManager.beginTransaction().show(homeFragment).commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .add(binding.fcvMain.id, HomeFragment.newInstance())
                        .commit()
                }
            }
            R.id.menu_look_around -> {
                val lookAroundFragment =
                    supportFragmentManager.fragments.find { it is LookAroundFragment }
                if (lookAroundFragment != null) {
                    supportFragmentManager.beginTransaction().show(lookAroundFragment).commit()
                } else {
                    supportFragmentManager.beginTransaction().add(
                        binding.fcvMain.id,
                        LookAroundFragment.newInstance()
                    ).commit()
                }
            }
            R.id.menu_my_page -> {
//                val myPageFragment = supportFragmentManager.fragments.find { it is MypageFragment }
//                if (myPageFragment != null) {
//                    supportFragmentManager.beginTransaction().show(myPageFragment).commit()
//                } else {
                supportFragmentManager.beginTransaction()
                    .add(binding.fcvMain.id, MypageFragment()).commit()
//                }
//            }
//            else -> {
//                throw IllegalArgumentException("Not found menu item")
//            }
            }
        }

        return true
    }
}
