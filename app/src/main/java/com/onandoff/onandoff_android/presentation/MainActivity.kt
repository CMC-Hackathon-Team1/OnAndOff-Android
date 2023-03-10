package com.onandoff.onandoff_android.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.messaging.FirebaseMessaging
import com.onandoff.onandoff_android.FragmentAdapter
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.ActivityMainBinding
import com.onandoff.onandoff_android.presentation.home.HomeFragment
import com.onandoff.onandoff_android.presentation.home.otheruser.OtherUserFragment
import com.onandoff.onandoff_android.presentation.look.LookAroundFragment
import com.onandoff.onandoff_android.presentation.mypage.MypageFragment

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

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
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            val token = task.result

        })
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
                val myPageFragment = supportFragmentManager.fragments.find { it is MypageFragment }
                if (myPageFragment != null) {
                    supportFragmentManager.beginTransaction().show(myPageFragment).commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .add(binding.fcvMain.id, OtherUserFragment()).commit()
                }
            }
            else -> {
                throw IllegalArgumentException("Not found menu item")
            }
        }

        return true
    }
}