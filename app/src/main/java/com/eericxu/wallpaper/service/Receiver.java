package com.eericxu.wallpaper.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Eericxu on 2017-08-03.
 */

public class Receiver extends BroadcastReceiver {
	private WallpaperServer server;

	public Receiver(WallpaperServer server) {
		this.server = server;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		server.onChange();
	}
}
