package com.jk.daggerrxkotlin.extensions

import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlin.jk.com.dagger.R

/**
 * Created by M2353204 on 02/08/2017.
 */

fun ImageView.loading(imageUrl: String) {
    if (TextUtils.isEmpty(imageUrl)) {
        Glide.with(context).load(R.mipmap.ic_launcher).into(this)
    } else {
        Glide.with(context).load(imageUrl).into(this)
    }
}