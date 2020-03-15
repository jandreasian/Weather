package com.jandreasian.weatherapplication.network

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * Uses the Glide library to load an image by URI into an [ImageView] for NewPost
 */
@BindingAdapter("android:src")
fun bind(imgView: ImageView, imgUri: Int) {
    imgView.setImageResource(imgUri);
}