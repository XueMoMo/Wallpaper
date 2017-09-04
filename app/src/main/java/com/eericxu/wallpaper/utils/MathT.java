package com.eericxu.wallpaper.utils;

/**
 * Created by Eericxu on 2017-09-04.
 */

public class MathT {
	public static double sin(int angle) {
		return Math.sin(angleToNum(angle));
	}

	public static double cos(int angle) {
		return Math.cos(angleToNum(angle));
	}

	public static double angleToNum(int angle) {
		return 2 * Math.PI / 360 * angle;
	}
}
