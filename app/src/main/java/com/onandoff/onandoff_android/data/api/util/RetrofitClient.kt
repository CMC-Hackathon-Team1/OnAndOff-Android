package com.onandoff.onandoff_android.data.api.util

import android.util.Log
import com.onandoff.onandoff_android.data.api.util.API_CONSTANTS.BASE_URL
import com.onandoff.onandoff_android.util.AddCookiesInterceptor
import com.onandoff.onandoff_android.util.Constants.TAG
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    //retrofit 객체 생성
    private var retrofitClient:Retrofit?=null

    fun getClient(): Retrofit?{
        Log.d(TAG,"retrofitClient - getClient() called")

        //api 통신상태를 상세하게 로깅 찍을수있는 httpclient
        val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor(AddCookiesInterceptor())
//                .addInterceptor(ReceivedCookiesInterceptor())
                .build()
        }

        //아직 retrofit 객체가 생성되지않았다면
        if(retrofitClient == null){
            //생성해준다
            Log.d(TAG,"retrofitClient - init start")
            retrofitClient = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
        return retrofitClient
    }
}