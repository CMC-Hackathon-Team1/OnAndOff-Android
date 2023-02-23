package com.onandoff.onandoff_android.util

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class SharePreference:Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        KakaoSdk.init(this, "8faa3516520f4561c39f29f1b932eb44")
        super.onCreate()
    }
}