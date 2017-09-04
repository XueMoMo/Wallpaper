package com.eericxu.wallpaper;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
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
	private String[] names = {
			"anim", "web"
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startService(new Intent(this, WallpaperServer.class));
//        PermissionUtils.checkAndReqPermission(this, Manifest.permission.CAMERA, new PermissionUtils.Callback() {
//            @Override
//            public void call(boolean hasPermission) {
//
//            }
//        });
		initView();
	}

	private void initView() {
		spinner = findView(R.id.spinner);
		arrayAdapter = new ArrayAdapter<>(this, R.layout.item_tv, R.id.tv_name, names);
		spinner.setAdapter(arrayAdapter);
		spinner.setOnItemSelectedListener(this);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
			case 0:
				Config.saveWallpaper(this, "anim");
				break;
			case 1:
				Config.saveWallpaper(this, "web");
				break;
		}
		sendBroadcast(new Intent(WallpaperServer.action_receiver));
		WallpaperManager instance = WallpaperManager.getInstance(this);
		WallpaperInfo wallpaperInfo = instance.getWallpaperInfo();
		if (wallpaperInfo == null || !wallpaperInfo.getServiceInfo().packageName.equals(getPackageName())) {
			Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
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
	public void onNothingSelected(AdapterView<?> parent) {

	}
}
