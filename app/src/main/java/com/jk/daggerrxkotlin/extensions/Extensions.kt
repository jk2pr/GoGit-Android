package com.jk.daggerrxkotlin.extensions

import android.text.TextUtils
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlin.jk.com.daggerrxkotlin.R

/**
 * Created by M2353204 on 02/08/2017.
 */

fun ImageView.loading(imageUrl: String) {
    if (TextUtils.isEmpty(imageUrl)) {
        Picasso.with(context).load(R.mipmap.ic_launcher).into(this)
    } else {
        Picasso.with(context).load(imageUrl).into(this)
    }
}