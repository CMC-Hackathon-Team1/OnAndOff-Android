package com.onandoff.onandoff_android.data.api

import android.util.Log
import com.onandoff.onandoff_android.util.Constants.TAG
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    //retrofit 객체 생성
    private var retrofitClient:Retrofit?=null
    //singleton 패턴
    fun getClient(baseUrl:String): Retrofit?{
        Log.d(TAG,"retrofitClient - getClient() called")
        //아직 retrofit 객체가 생성되지않았다면
        if(retrofitClient == null){
            //생성해준다
            Log.d(TAG,"retrofitClient - init start")
            retrofitClient = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofitClient
    }
}