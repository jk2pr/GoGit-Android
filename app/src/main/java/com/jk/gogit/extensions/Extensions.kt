package com.jk.gogit.extensions

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.jk.gogit.R

/**
 * Created by M2353204 on 02/08/2017.
 */
fun ImageView.loading(imageUrl: String) {
    if (TextUtils.isEmpty(imageUrl)) {
        Glide.with(context).load(R.mipmap.ic_launcher).into(this)
    } else {
        Glide.with(context).load(imageUrl).transition(DrawableTransitionOptions.withCrossFade()).into(this)
    }
}

fun ImageView.loadingA(imageUrl: String,listener : RequestListener<Drawable>) {

    if (!TextUtils.isEmpty(imageUrl)) {

        Glide.with(context)
                .load(imageUrl)
            //    .thumbnail(0.1f)
                .listener(listener)
                .into(this)

    }
}