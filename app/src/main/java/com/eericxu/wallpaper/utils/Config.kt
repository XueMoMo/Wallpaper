package com.eericxu.wallpaper.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Eericxu on 2017-08-03.
 */
object Config {
    private const val Pref = "EercxuWallpaperPref"

    fun saveWallpaper(context: Context, wall: String?) {
        getW(context).putString("saveWallpaper", wall).apply()
    }

    fun getWallpaper(context: Context): String? {
        return getR(context).getString("saveWallpaper", "anim")
    }

    private fun getW(context: Context): SharedPreferences.Editor {
        return getR(context).edit()
    }

    private fun getR(context: Context): SharedPreferences {
        return context.getSharedPreferences(Pref, Context.MODE_PRIVATE)
    }
}
