package com.onandoff.onandoff_android.util

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.common.KakaoSdk


class SharePreference:Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        KakaoSdk.init(this, "ae1a0ec9bf2a22467f3c77957ae9fffb")

        super.onCreate()
    }
}