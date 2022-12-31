package com.onandoff.onandoff_android.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding>(val bindingFactory: (LayoutInflater) -> VB) : AppCompatActivity() {
    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        onCreateLifeCycle()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyLifeCycle()
        _binding = null
    }

    abstract fun onCreateLifeCycle()
    abstract fun onDestroyLifeCycle()

}