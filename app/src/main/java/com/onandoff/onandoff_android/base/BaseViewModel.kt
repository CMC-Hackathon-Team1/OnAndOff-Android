package com.onandoff.onandoff_android.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onandoff.onandoff_android.util.Event
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val _exceptionEvent = MutableLiveData<Event<Throwable>>()
    val exceptionEvent : LiveData<Event<Throwable>>
        get() = _exceptionEvent
    private val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        _exceptionEvent.value = Event(throwable)
    }
    fun viewModelScopeHandleException(function : suspend () -> Unit){
        viewModelScope.launch(coroutineExceptionHandler) {
            function()
        }
    }
}
