package com.eericxu.wallpaper.service

import com.eericxu.wallpaper.service.WallpaperServer.MEngine


/**
 * Created by Eericxu on 2017-06-05.
 */
interface Wallpaper {
    fun onCreate(engine: MEngine)
    fun destroy()

    fun onVisibility(visible: Boolean)
}
