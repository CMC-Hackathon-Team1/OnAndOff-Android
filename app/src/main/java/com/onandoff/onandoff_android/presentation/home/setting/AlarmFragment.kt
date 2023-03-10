package com.onandoff.onandoff_android.presentation.home.setting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.FragmentSettingAlarmBinding
import com.onandoff.onandoff_android.presentation.MainActivity
import com.onandoff.onandoff_android.presentation.home.HomeFragment
import com.onandoff.onandoff_android.presentation.home.SettingFragment

class AlarmFragment:Fragment() {
    private lateinit var binding :FragmentSettingAlarmBinding
    lateinit var mainActivity: MainActivity
    var isSwitchLike:Boolean = true
    var isSwitchFollowing:Boolean = true
    var isSwitchPosting:Boolean = true
    var isSwitchFollowingPosting:Boolean = true
    var isSwitchNotice:Boolean = true
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
            if(isSwitchLike){
                isSwitchLike = false
                binding.switchLike.setImageResource(R.drawable.ic_switch_off_40)
            }else{
                isSwitchLike = true
                binding.switchLike.setImageResource(R.drawable.ic_switch_on_40)
            }
        }

        binding.switchFollowing.setOnClickListener{
            if(isSwitchFollowing){
                isSwitchFollowing = false
                binding.switchFollowing.setImageResource(R.drawable.ic_switch_off_40)
            }else{
                isSwitchFollowing = true
                binding.switchFollowing.setImageResource(R.drawable.ic_switch_on_40)
            }
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
            if(isSwitchNotice){
                isSwitchNotice = false
                binding.switchNotice.setImageResource(R.drawable.ic_switch_off_40)
            }else{
                isSwitchNotice = true
                binding.switchNotice.setImageResource(R.drawable.ic_switch_on_40)
            }
        }
        return binding.root
    }

}