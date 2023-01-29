package com.onandoff.onandoff_android.util

import android.util.Log
import com.onandoff.onandoff_android.util.SharePreference.Companion.prefs
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.cookieToString
import okio.IOException


class ReceivedCookiesInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            val cookies: HashSet<String> = HashSet()
            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
            }
            // Preference에 cookies를 넣어주는 작업을 수행
            prefs.putSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_COOKIE,cookies);
        }

        return originalResponse
    }
}
class AddCookiesInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        // Preference에서 cookies를 가져오는 작업을 수행
        val preferences: Set<String> = prefs.getSharedPreference(
            APIPreferences.SHARED_PREFERENCE_NAME_COOKIE,
            HashSet<String>()
        )
        for (cookie in preferences) {
            builder.addHeader("Cookie", cookie)
        }

        // Web,Android,iOS 구분을 위해 User-Agent세팅
        builder.removeHeader("User-Agent").addHeader("User-Agent", "Android")
        return chain.proceed(builder.build())
    }
}