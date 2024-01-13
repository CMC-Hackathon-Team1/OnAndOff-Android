package com.onandoff.onandoff_android

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.onandoff.onandoff_android.presentation.home.HomeFragment

class FragmentAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeFragment.newInstance()
            1 -> HomeFragment.newInstance() // 수정 필요
            else -> HomeFragment.newInstance() // 수정 필요
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}
