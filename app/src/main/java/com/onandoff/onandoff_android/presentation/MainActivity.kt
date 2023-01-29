package com.onandoff.onandoff_android.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.navigation.NavigationBarView
import com.onandoff.onandoff_android.FragmentAdapter
import com.onandoff.onandoff_android.R
import com.onandoff.onandoff_android.databinding.ActivityMainBinding
import com.onandoff.onandoff_android.presentation.home.HomeFragment
import com.onandoff.onandoff_android.presentation.mypage.MypageFragment
import com.onandoff.onandoff_android.presentation.look.LookAroundFragment

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener  {
    private lateinit var binding: ActivityMainBinding

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
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        when(item.itemId) {
            R.id.menu_home -> {
                fragmentTransaction.replace(binding.fcvMain.id, HomeFragment.newInstance()).commit()
            }
            R.id.menu_look_around -> {
                fragmentTransaction.replace(binding.fcvMain.id, LookAroundFragment.newInstance()).commit()
            }
            R.id.menu_my_page -> {
                fragmentTransaction.replace(binding.fcvMain.id, MypageFragment()).commit()
            }
            else -> {
                throw IllegalArgumentException("Not found menu item")
            }
        }

        return true
    }
}