package com.onandoff.onandoff_android.presentation

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import com.onandoff.onandoff_android.FragmentAdapter
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.ActivityMainBinding
import com.onandoff.onandoff_android.presentation.home.HomeFragment
import com.onandoff.onandoff_android.presentation.look.FeedListFragment
import com.onandoff.onandoff_android.presentation.mypage.MypageFragment


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

        if (savedInstanceState == null) {
            binding.bottomNavMain.selectedItemId = R.id.menu_home
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
                val feedListFragment =
                    supportFragmentManager.fragments.find { it is FeedListFragment }
                if (feedListFragment != null) {
                    supportFragmentManager.beginTransaction().show(feedListFragment).commit()
                } else {
                    supportFragmentManager.beginTransaction().add(
                        binding.fcvMain.id,
                        FeedListFragment.newInstance()
                    ).commit()
                }
            }
            R.id.menu_my_page -> {
                val myPageFragment = supportFragmentManager.fragments.find { it is MypageFragment }
                if (myPageFragment != null) {
                    supportFragmentManager.beginTransaction().show(myPageFragment).commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .add(binding.fcvMain.id, MypageFragment()).commit()
                }
            }
            else -> {
                throw IllegalArgumentException("Not found menu item")
            }
        }

        return true
    }
}