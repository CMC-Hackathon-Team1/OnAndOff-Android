package com.onandoff.onandoff_android.di

import com.onandoff.onandoff_android.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providePicSumGalleryRetrofit(client : OkHttpClient) : Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.ON_AND_OFF_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            if(BuildConfig.DEBUG)
                setLevel(HttpLoggingInterceptor.Level.BODY)
        }).build()
    }
}