package com.eericxu.wallpaper.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Eericxu on 2017-08-03.
 */

public class Config {
	private static final String Pref = "EercxuWallpaperPref";

	public static void saveWallpaper(Context context, String wall) {
		getW(context).putString("saveWallpaper", wall).apply();
	}

	public static String getWallpaper(Context context) {
		return getR(context).getString("saveWallpaper", "anim");
	}

	private static SharedPreferences.Editor getW(Context context) {
		return getR(context).edit();
	}

	private static SharedPreferences getR(Context context) {
		return context.getSharedPreferences(Pref, Context.MODE_PRIVATE);
	}
}
