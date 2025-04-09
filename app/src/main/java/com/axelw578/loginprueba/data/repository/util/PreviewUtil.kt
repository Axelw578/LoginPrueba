package com.axelw578.loginprueba.util

import android.content.Context
import android.widget.ImageView
import android.widget.VideoView
import com.bumptech.glide.Glide


object PreviewUtil {
    // Previsualiza una imagen usando Glide
    fun previewImage(context: Context, imageUri: String, imageView: ImageView) {
        Glide.with(context)
            .load(imageUri)
            .into(imageView)
    }

    // Previsualiza un video usando VideoView (simple ejemplo)
    fun previewVideo(videoView: VideoView, videoUri: String) {
        videoView.setVideoPath(videoUri)
        videoView.requestFocus()
        videoView.start()
    }


}
