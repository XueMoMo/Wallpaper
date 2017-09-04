package com.eericxu.wallpaper.base;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.eericxu.wallpaper.utils.PermissionUtils;

/**
 * Created by Eericxu on 2017-06-05.
 */

public class BaseActivity extends AppCompatActivity {


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionResult(this,requestCode,permissions,grantResults);
    }

    public  <T> T findView(int id){
        return (T)findViewById(id);
    }
}
