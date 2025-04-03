package com.eericxu.wallpaper.utils

/**
 * Created by Eericxu on 2017-09-04.
 */
object MathT {
    fun sin(angle: Int): Double {
        return kotlin.math.sin(angleToNum(angle))
    }

    fun cos(angle: Int): Double {
        return kotlin.math.cos(angleToNum(angle))
    }

    fun angleToNum(angle: Int): Double {
        return 2 * Math.PI / 360 * angle
    }
}
