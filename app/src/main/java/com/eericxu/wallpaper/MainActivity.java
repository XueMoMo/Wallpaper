package com.eericxu.wallpaper;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.eericxu.wallpaper.base.BaseActivity;
import com.eericxu.wallpaper.service.WallpaperServer;
import com.eericxu.wallpaper.utils.PermissionUtils;

import java.security.Permission;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, WallpaperServer.class));
        PermissionUtils.checkAndReqPermission(this, Manifest.permission.CAMERA, new PermissionUtils.Callback() {
            @Override
            public void call(boolean hasPermission) {

            }
        });
    }
}
