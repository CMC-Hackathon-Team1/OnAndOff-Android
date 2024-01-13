package com.onandoff.onandoff_android.presentation.home.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.notification.NotificationInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.NotificationData
import com.onandoff.onandoff_android.data.model.NotificationResponse
import com.onandoff.onandoff_android.databinding.ActivitySettingAlarmBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.home.SettingActivity
import com.onandoff.onandoff_android.util.APIPreferences
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "AlarmFragment"
class AlarmActivity:AppCompatActivity() {
    private lateinit var binding : ActivitySettingAlarmBinding
    lateinit var mainActivity: MainActivity
    private var isSwitchLike:Boolean = prefs.getSharedPreference(APIPreferences.SHARED_PREFERENCE_LIKE_NOTIFICATION_SETTING, true)
    private var isSwitchFollowing:Boolean = prefs.getSharedPreference(APIPreferences.SHARED_PREFERENCE_FOLLOW_NOTIFICATION_SETTING, true)
    private var isSwitchNotice:Boolean = prefs.getSharedPreference(APIPreferences.SHARED_PREFERENCE_NOTICE_NOTIFICATION_SETTING, true)

    private val notificationInterface: NotificationInterface? = RetrofitClient.getClient()?.create(NotificationInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingAlarmBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivBackArrow.setOnClickListener{
            finish()
        }
        binding.switchLike.setOnClickListener{
            isSwitchLike = !isSwitchLike
            updateLikeSetting()
        }

        binding.switchFollowing.setOnClickListener{
            isSwitchFollowing = !isSwitchFollowing
            updateFollowSetting()
        }
        binding.switchNotice.setOnClickListener{
            isSwitchNotice = !isSwitchNotice
            updateNoticeSetting()
        }
    }

    private fun updateLikeSetting() {
        val data: NotificationData = if (isSwitchLike) {
            NotificationData(1)
        } else {
            NotificationData(0)
        }

        notificationInterface?.updateLikeAlarmResponse(data)?.enqueue(object :
            Callback<NotificationResponse> {
            override fun onResponse(
                call: Call<NotificationResponse>,
                response: Response<NotificationResponse>
            ) {
                if (response.body()?.statusCode == 3501) {
                    binding.switchLike.setImageResource(R.drawable.ic_switch_on_40)
                } else {
                    binding.switchLike.setImageResource(R.drawable.ic_switch_off_40)
                }
            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: update notice")
            }

        })
    }

    private fun updateFollowSetting() {
        val data: NotificationData = if (isSwitchFollowing) {
            NotificationData(1)
        } else {
            NotificationData(0)
        }

        notificationInterface?.updateFollowAlarmResponse(data)?.enqueue(object :
            Callback<NotificationResponse> {
            override fun onResponse(
                call: Call<NotificationResponse>,
                response: Response<NotificationResponse>
            ) {
                if (response.body()?.statusCode == 3501) {
                    binding.switchFollowing.setImageResource(R.drawable.ic_switch_on_40)
                } else {
                    binding.switchFollowing.setImageResource(R.drawable.ic_switch_off_40)
                }
            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: update follow")
            }

        })
    }

    private fun updateNoticeSetting() {
        val data: NotificationData = if (isSwitchNotice) {
            NotificationData(1)
        } else {
            NotificationData(0)
        }

        notificationInterface?.updateNoticeAlarmResponse(data)?.enqueue(object :
            Callback<NotificationResponse> {
            override fun onResponse(
                call: Call<NotificationResponse>,
                response: Response<NotificationResponse>
            ) {
                if (response.body()?.statusCode == 3501) {
                    binding.switchNotice.setImageResource(R.drawable.ic_switch_on_40)
                } else {
                    binding.switchNotice.setImageResource(R.drawable.ic_switch_off_40)
                }
            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: notice update")
            }

        })
    }

    companion object {
        fun getIntent(context: Context) =
            Intent(context, AlarmActivity::class.java)
    }
}
