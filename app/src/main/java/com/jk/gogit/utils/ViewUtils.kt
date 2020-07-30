package com.jk.gogit.utils

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.jk.gogit.R

object ViewUtils {
    fun getLabelTextColor(context: Context, bgColorValue: Int): Int {
        return if (isLightColor(bgColorValue)) {
            ContextCompat.getColor(context, R.color.colorPrimary)
        } else {
            ContextCompat.getColor(context, R.color.white)
        }
    }

    private fun isLightColor(color: Int): Boolean {
        val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness < 0.5
    }
}