package com.eericxu.wallpaper.video

import android.graphics.Canvas
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.eericxu.wallpaper.service.BaseWallpaper
import com.eericxu.wallpaper.service.WallpaperServer

/**
 * Created by Eericxu on 2017-06-05.
 */
class DefVideo : BaseWallpaper() {
    private lateinit var player: ExoPlayer
    val TAG = "DefVideo"

    @OptIn(UnstableApi::class)
    override fun onCreate(engine: WallpaperServer.MEngine) {
        super.onCreate(engine)
        player = ExoPlayer.Builder(engine.context).build()
        val url = "file:///android_asset/video.mp4"
//        val url = "${engine.context.getExternalFilesDir("")}/video.mp4"
        // Build the media item.
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        player.repeatMode = ExoPlayer.REPEAT_MODE_ALL
        player.prepare()
    }

    @OptIn(UnstableApi::class)
    override fun onVisibility(visible: Boolean) {
        super.onVisibility(visible)
        if (visible) {
            player.setVideoSurfaceHolder(engine.surfaceHolder)
            player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            player.playWhenReady = true
            if(!player.isLoading && !player.isPlaying) player.play()
        } else {
            player.pause()
            player.clearVideoSurfaceHolder(engine.surfaceHolder)
        }
    }

    override fun destroy() {
        super.destroy()
        player.release()
    }

    override fun onFrame(canvas: Canvas) {

    }

}
