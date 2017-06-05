package com.eericxu.wallpaper.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by Eericxu on 2017/2/28.
 */

public class PermissionUtils {
    private static final int MY_PERMISSIONS_REQUEST = 101;
    // Here, thisActivity is the current activity
    private static Map<Activity,Callback> callbackMap = new HashMap<>();
    public static void checkAndReqPermission(@NonNull Activity thisActivity, @NonNull String permission,Callback callback){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(thisActivity, permission) != PERMISSION_GRANTED) {
            // Should we show an explanation?
            callbackMap.put(thisActivity,callback);
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity, permission)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(thisActivity, new String[]{permission}, MY_PERMISSIONS_REQUEST);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else {
            callback.call(true);
        }
    }
    public static void onRequestPermissionResult(Activity activity,int requestCode, String permissions[], int[] grantResults){
        if (requestCode==MY_PERMISSIONS_REQUEST){
            Callback callback = callbackMap.get(activity);
            if (callback!=null&&grantResults.length>0){
                callback.call(grantResults[0]==PERMISSION_GRANTED);
                callbackMap.remove(activity);
            }
        }
    }

    public abstract static class Callback{
        public abstract void call(boolean hasPermission);
    }
}
