package com.eericxu.wallpaper.service;


/**
 * Created by Eericxu on 2017-06-05.
 */

public interface Wallpaper {
    public static final int Runing=1;
    int state=0;
    void onCreate(WallpaperServer.MEngine engine);
    void destroy();

    void onVisibility(boolean visible);
}
