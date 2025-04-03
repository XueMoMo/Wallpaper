package com.eericxu.wallpaper.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by Eericxu on 2017-08-03.
 */
class Receiver(private val server: WallpaperServer) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        server.onChange()
    }
}
