package com.onandoff.onandoff_android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

typealias Inflate<T> = (LayoutInflater,ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
): Fragment() , CoroutineScope{
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreateLifeCycle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDestroyViewLifeCycle()
        _binding = null
        job.cancel()
    }

    fun BaseViewModel.observeErrorHandling(){
        exceptionEvent.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let { exception ->
                Toast.makeText(context,"Error : ${exception.message}",Toast.LENGTH_SHORT).show()
            }
        }
    }
    abstract fun onViewCreateLifeCycle()
    abstract fun onDestroyViewLifeCycle()
}
