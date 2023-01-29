//package com.onandoff.onandoff_android.data
//
//import okhttp3.Interceptor
//import okhttp3.Request
//import okhttp3.Response
//import java.io.IOException
//
//
//class AddCookiesInterceptor : Interceptor {
//    @Throws(IOException::class)
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val builder: Request.Builder = chain.request().newBuilder()
//
//        // Preference에서 cookies를 가져오는 작업을 수행
//        val preferences: Set<String> = SharedPreferenceBase.getSharedPreference(
//            APIPreferences.SHARED_PREFERENCE_NAME_COOKIE,
//            HashSet<String>()
//        )
//        for (cookie in preferences) {
//            builder.addHeader("Cookie", cookie)
//        }
//
//        // Web,Android,iOS 구분을 위해 User-Agent세팅
//        builder.removeHeader("User-Agent").addHeader("User-Agent", "Android")
//        return chain.proceed(builder.build())
//    }
//}