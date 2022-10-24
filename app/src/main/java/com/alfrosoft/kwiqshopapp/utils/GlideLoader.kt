package com.alfrosoft.kwiqshopapp.utils

import android.content.Context
import android.widget.ImageView
import com.alfrosoft.kwiqshopapp.R
import com.bumptech.glide.Glide
import java.io.IOException


class GlideLoader (val context: Context) {

    fun loadUserProfile(image : Any, imageView : ImageView){
        try {
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_placeholder)
                .into(imageView)

        }catch (e : IOException){
            e.printStackTrace()
        }
    }
    fun loadProductPicture(image : Any, imageView : ImageView){
        try {
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .into(imageView)

        }catch (e : IOException){
            e.printStackTrace()
        }
    }
}