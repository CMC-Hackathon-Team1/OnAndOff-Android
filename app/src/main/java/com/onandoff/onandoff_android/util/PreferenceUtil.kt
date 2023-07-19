package com.onandoff.onandoff_android.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)
    fun putSharedPreference(key: String?, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun putSharedPreference(key: String?, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun putSharedPreference(key: String?, value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }

    fun putSharedPreference(key: String?, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun putSharedPreference(key: String?, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun putSharedPreference( key: String?, values: MutableSet<String>) {
        val mutableList = mutableSetOf<String>()
        for(value in values){
        mutableList.add(value)
        }
        prefs.edit().putStringSet(key,mutableList)
    }


//    fun putSharedPreference(key: String?, value: HashSet<String>) {
//        prefs.edit().putStringSet(key, value).apply()
//    }

    fun observePreference(targetKey: String?): Flow<Int> {
        return callbackFlow {
            val listener: (sharedPreferences: SharedPreferences, key: String?) -> Unit =
                { _, key ->
                    if (key == targetKey) {
                        trySend(getSharedPreference(targetKey, -1))
                    }
                }
            prefs.registerOnSharedPreferenceChangeListener(listener)

            // flow가 유효하지 않게 될때 불림.
            awaitClose {
                prefs.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }
    }

    fun getSharedPreference(key: String?, defaultValue: Int): Int {
        return prefs.getInt(key, defaultValue)
    }

    fun getSharedPreference(key: String?, defaultValue: Float): Float {
        return prefs.getFloat(key, defaultValue)
    }

    fun getSharedPreference(key: String?, defaultValue: Long): Long? {
        return prefs.getLong(key, defaultValue)
    }

    fun getSharedPreference(key: String?, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    fun getSharedPreference(key: String, defaultValue: String): String {
        return prefs.getString(key, defaultValue)!!
    }
    fun getSharedPreference(key: String): MutableSet<String>? {
        val mutableList = mutableSetOf<String>()
        return prefs.getStringSet(key,mutableList)
    }

    fun getSharedPreference(key: String, defaultValue: HashSet<String>): Set<String> {
        return prefs.getStringSet(key, defaultValue)!!
    }
}
object APIPreferences{
    val SHARED_PREFERENCE_NAME_JWT:String = "jwt"
    val SHARED_PREFERENCE_NAME_EMAIL:String = "email"
    val SHARED_PREFERENCE_NAME_USERID:String = "profileId"
    val SHARED_PREFERENCE_NAME_PROFILEID:String = "currentPersonaId"
    const val SHARED_PREFERENCE_LIKE_NOTIFICATION_SETTING:String = "likeSetting"
    const val SHARED_PREFERENCE_FOLLOW_NOTIFICATION_SETTING:String = "followSetting"
    const val SHARED_PREFERENCE_NOTICE_NOTIFICATION_SETTING:String = "noticeSetting"
}