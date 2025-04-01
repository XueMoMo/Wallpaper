package com.eericxu.wallpaper;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.eericxu.wallpaper.base.BaseActivity;
import com.eericxu.wallpaper.service.WallpaperServer;
import com.eericxu.wallpaper.utils.Config;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private ArrayAdapter<String> arrayAdapter;
    private String[] names = { "anim", "web", "one" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, WallpaperServer.class));
        initView();
    }

    private void initView() {
        spinner = findView(R.id.spinner);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.item_tv, R.id.tv_name, names);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void onChange(int position) {
        if(position < 0) return;
        Config.saveWallpaper(this, names[position]);
        sendBroadcast(new Intent(WallpaperServer.action_receiver));
        WallpaperManager instance = WallpaperManager.getInstance(this);
        WallpaperInfo wallpaperInfo = instance.getWallpaperInfo();
        if (wallpaperInfo == null || !wallpaperInfo.getServiceInfo().packageName.equals(getPackageName())) {
            Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(getApplication(), WallpaperServer.class));
            startActivity(intent);
        } else {
            Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
            mHomeIntent.addCategory(Intent.CATEGORY_HOME);
            mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(mHomeIntent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        onChange(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
