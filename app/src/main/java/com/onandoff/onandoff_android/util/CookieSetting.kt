package com.onandoff.onandoff_android.util

import android.util.Log
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.cookieToString
import okio.IOException

class AddCookiesInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        // Preference에서 jwt를 가져오는 작업을 수행
        val cookie: String = prefs.getSharedPreference(
            APIPreferences.SHARED_PREFERENCE_NAME_JWT,
            ""
        )
        builder.addHeader("Authorization", "Bearer ${cookie}")

        // Web,Android,iOS 구분을 위해 User-Agent세팅
        builder.removeHeader("User-Agent").addHeader("User-Agent", "Android")
        return chain.proceed(builder.build())
    }
}
