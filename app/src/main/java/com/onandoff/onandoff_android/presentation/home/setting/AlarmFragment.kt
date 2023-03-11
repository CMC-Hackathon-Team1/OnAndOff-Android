package com.onandoff.onandoff_android.presentation.home.setting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.data.api.notification.NotificationInterface
import com.onandoff.onandoff_android.data.api.util.RetrofitClient
import com.onandoff.onandoff_android.data.model.NotificationData
import com.onandoff.onandoff_android.data.model.NotificationResponse
import com.onandoff.onandoff_android.databinding.FragmentSettingAlarmBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.home.SettingFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmFragment:Fragment() {
    private lateinit var binding :FragmentSettingAlarmBinding
    lateinit var mainActivity: MainActivity
    var isSwitchLike:Boolean = true
    var isSwitchFollowing:Boolean = true
    var isSwitchPosting:Boolean = true
    var isSwitchFollowingPosting:Boolean = true
    var isSwitchNotice:Boolean = true

    val notificationInterface: NotificationInterface? = RetrofitClient.getClient()?.create(NotificationInterface::class.java)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingAlarmBinding.inflate(layoutInflater)
        binding.ivBackArrow.setOnClickListener{
            mainActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_main, SettingFragment())
                .commit()
        }
        binding.switchLike.setOnClickListener{
            isSwitchLike = !isSwitchLike
            updateLikeSetting()
        }

        binding.switchFollowing.setOnClickListener{
            isSwitchFollowing = !isSwitchFollowing
            updateFollowSetting()
        }
        binding.switchPosting.setOnClickListener{
            if(isSwitchPosting){
                isSwitchPosting = false
                binding.switchPosting.setImageResource(R.drawable.ic_switch_off_40)
            }else{
                isSwitchPosting = true
                binding.switchPosting.setImageResource(R.drawable.ic_switch_on_40)
            }
        }
        binding.switchFollowingPosting.setOnClickListener{
            if(isSwitchFollowingPosting){
                isSwitchFollowingPosting = false
                binding.switchFollowingPosting.setImageResource(R.drawable.ic_switch_off_40)
            }else{
                isSwitchFollowingPosting = true
                binding.switchFollowingPosting.setImageResource(R.drawable.ic_switch_on_40)
            }
        }
        binding.switchNotice.setOnClickListener{
            isSwitchNotice = !isSwitchNotice
            updateNoticeSetting()
        }
        return binding.root
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
                TODO("Not yet implemented")
            }

        })
    }

    private fun updateFollowSetting() {
        val data: NotificationData = if (isSwitchFollowing) {
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
                    binding.switchFollowing.setImageResource(R.drawable.ic_switch_on_40)
                } else {
                    binding.switchFollowing.setImageResource(R.drawable.ic_switch_off_40)
                }
            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                TODO("Not yet implemented")
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
                TODO("Not yet implemented")
            }

        })
    }
}