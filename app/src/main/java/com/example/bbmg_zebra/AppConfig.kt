package com.example.bbmg_zebra

import android.content.Context
import android.content.SharedPreferences

object AppConfig {
    private const val PREFS_NAME = "AppConfigPrefs"
    private const val KEY_SERVER_URL = "server_url"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveServerUrl(context: Context, url: String) {
        getPrefs(context).edit().putString(KEY_SERVER_URL, url).apply()
    }

    fun getServerUrl(context: Context): String? {
        return getPrefs(context).getString(KEY_SERVER_URL, null)
    }

    fun clearServerUrl(context: Context) {
        getPrefs(context).edit().remove(KEY_SERVER_URL).apply()
    }
}